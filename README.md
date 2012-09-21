JSON accessor functions for PostgreSQL
======================================

[PostgreSQL](http://www.postgresql.org/) stored functions for accessing [JSON](http://www.json.org/) fields.

If you have text (varchar) columns with data like this:

    {"create_date":"2009-12-01 01:23:45","tags":["foo","bar","baz"]}

these functions can be used for:

 - creating queries to JSON object fields
 - creating B-tree (default) indexes on JSON object fields (`create_date` field)
 - creating [GIN](http://www.postgresql.org/docs/9.1/static/gin.html) indexes on JSON arrays (`tags` field)

This project contains two PostgreSQL [extensions](http://www.postgresql.org/docs/9.1/static/extend-extensions.html) -
`json_accessors_java` and `json_accessors_c`. Extensions contain stored functions with the same signatures.
Extensions doesn't depend on each other: Java extension may be used in development, and C one - in production.

Java extension was written using [PL/Java](http://pgfoundry.org/projects/pljava/) on top of [google-gson](http://code.google.com/p/google-gson/)
JSON parser. It should work with any PostgreSQL version that supports PL/Java (extension packaging may be used only with 9.1+).

Native C extension was writen on top of [cJSON](http://sourceforge.net/projects/cjson/) library. It is faster than Java version
and less strict (has less JSON field type checks). It's compatible with PostgreSQL 9.1+.

Indexing JSON in PostgreSQL
---------------------------

Sometimes JSON is convenient for storing structured data in single text field. But it's not easy to provide fast (indexed) search on separate JSON fields.
PostgreSQL have had no JSON support until version 9.2, which [introduced some support](http://www.postgresql.org/docs/9.2/static/functions-json.html).
These 9.2 functions won't help with indexing JSON data.

To create JSON index we need stored functions to parse JSON text fields.
Such function may be written using [PL/V8](http://code.google.com/p/plv8js/wiki/PLV8) module, [this article](http://people.planetpostgresql.org/andrew/index.php?/archives/249-Using-PLV8-to-index-JSON.html)
has an example of PL/V8 usage.

This project provides stored functions for indexing JSON without using PL/V8.

Functions
---------

__Function for accessing JSON object fields:__

    function json_get_text(text, text) returns text

Usage example, returns `qq`:

    select json_get_text('{"foo":"qq", "bar": true}', 'foo')

There are also similar functions returning `boolean`, `int`, `bigint`, `numeric` and `timestamp without timezone`.
Timestamp format `yyyy-MM-dd HH:mm:ss` is fixed.

To access complex JSON object fields you can use:

    function json_get_object(text, text) returns text

It extractc child JSON object and returns it as text.
Usage example, returns `{"boo":42}`:

    select json_get_object('{"foo":{"boo":42}, "bar": true}', 'foo')

To access JSON object fields, that contain arrays, there are functions for different array types
(including arrays of objects and multidimensional arrays), this example returns `array[42,43]`:

    json_get_int_array('{"boo": [42, 43]}', 'boo')

Arrays with different element types are not supported

__Function for converting JSON arrays into PostgreSQL arrays:__

    function json_array_to_text_array(text) returns text[]

Usage example, returns `array['foo', 'bar']`:

    select json_array_to_text_array('["foo", "bar"]')

There are also similar functions returning `boolean[]`, `int[]`, `bigint[]`, `numeric[]` and `timestamp without timezone[]`.
All primitive arrays returns from Java functions in boxed form (`Boolean[]` etc.) to allow returning `NULL` elements.
Having nulls in such arrays is not a good idea, but "Cannot assign null to int" errors in stored functions are worse.
Functions for arrays of objects and multidimensional arrays return `text[]`.

JSON index examples
-------------------

This example was tested on PostgreSQL 9.1, full example is [here](https://github.com/alx3apps/postgresql-json-accessors/blob/master/src/test/sql/test_json_index.sql)

Create table:

    create table test_table(id bigserial, val text);

Insert data (1572867 rows):

    insert into test_table(val) values('{"create_date":"2009-12-01 01:23:45","tags":["foo1","bar1","baz1"]}');
    insert into test_table(val) values('{"create_date":"2009-12-02 01:23:45","tags":["foo2","bar2","baz2"]}');
    insert into test_table(val) values('{"create_date":"2009-12-03 01:23:45","tags":["foo3","bar3","baz3"]}');
    insert into test_table(val) select val from test_table;
    -- repeat previous row 18 times more
    ....
    insert into test_table(val) values('{"create_date":"2009-12-04 01:23:45","tags":["foo4","bar4","baz4"]}');
    insert into test_table(val) values('{"create_date":"2009-12-04 01:23:45","tags":["foo4","bar4","baz4"]}');
    insert into test_table(val) values('{"create_date":"2009-12-04 01:23:45","tags":["foo4","bar4","baz4"]}');

Try to query three rows, inserted last, using `create_date` JSON field (query is slow):

    select * from test_table where json_get_timestamp(val, 'create_date')
        between '2009-12-04 01:00:00' and '2009-12-04 02:00:00';

It's plan looks like:

    Seq Scan on test_table  (cost=0.00..830998.51 rows=7864 width=76) (actual time=21904.068..21904.124 rows=3 loops=1)
      Filter: ((json_get_timestamp(val, 'create_date'::text) >= '2009-12-04 01:00:00'::timestamp without time zone) AND (json_get_timestamp(val, 'create_date'::text) <= '2009-12-04 02:00:00'::timestamp without time zone))
    Total runtime: 21904.157 ms

Create B-tree index on `create_date` JSON field:

    create index test_date_idx on test_table using btree (json_get_timestamp(val, 'create_date'));

Repeat query, now it's fast and plan looks like:

    Bitmap Heap Scan on test_table  (cost=149.62..16553.50 rows=7864 width=63) (actual time=0.025..0.028 rows=3 loops=1)
      Recheck Cond: (json_get_text(val, 'foo'::text) = 'qq3'::text)
      ->  Bitmap Index Scan on test_table_val_foo_idx  (cost=0.00..147.65 rows=7864 width=0) (actual time=0.019..0.019 rows=3 loops=1)
            Index Cond: (json_get_text(val, 'foo'::text) = 'qq3'::text)
    Total runtime: 0.054 ms

Try to query three rows, inserted last, checking inclusion into `tags` JSON field (query is slow):

    select * from test_table where json_get_text_array(val, 'tags') @> array['bar4'];

It's plan looks like:

    Seq Scan on test_table  (cost=0.00..827066.34 rows=1573 width=76) (actual time=41336.691..41336.749 rows=3 loops=1)
      Filter: (json_get_text_array(val, 'tags'::text) @> '{bar4}'::text[])
    Total runtime: 41336.799 ms

Create GIN index on `tags` JSON field:

    create index test_tags_idx on test_table using gin (json_get_text_array(val, 'tags'));

Repeat query, now it's fast and plan looks like:

    Bitmap Heap Scan on test_table  (cost=29.08..5679.25 rows=1573 width=76) (actual time=0.050..0.055 rows=3 loops=1)
      Recheck Cond: (json_get_text_array(val, 'tags'::text) @> '{bar4}'::text[])
      ->  Bitmap Index Scan on test_tags_idx  (cost=0.00..28.69 rows=1573 width=0) (actual time=0.037..0.037 rows=3 loops=1)
            Index Cond: (json_get_text_array(val, 'tags'::text) @> '{bar4}'::text[])
    Total runtime: 0.129 ms

Building and installing JAVA extension
--------------------------------------

Install PL/Java [on linux](http://wiki.tada.se/index.php?title=Installing_on_Linux_%28or_other_*nix%29) or [on windows](http://wiki.tada.se/index.php?title=Windows_Installation).
Some PL/Java docs are also available [here](http://cvs.pgfoundry.org/cgi-bin/cvsweb.cgi/pljava/org.postgresql.pljava/docs/userguide.html?rev=1.15).

Java extension depends on gson 2.2.1 (available in maven central) and on [jgit-buildnumber](https://github.com/alx3apps/jgit-buildnumber) for building.

Build maven project:

    mvn clean install

Extension will be built in `postgresql-json-accessors/target/postgresql-json-accessors-1.2-distr/` directory.

On linux/unix run this as superuser:

    json_accessors_java.sh -d <postgresql_install_dir>

It copies JAR files and extension descriptors and creates `classpath_install.sql` with proper paths, that must be executed
in particular database under superuser account.

On windows copy files `json_accessors_java--1.2.sql` and `json_accessors_java.control` into
`<postgresql_install_dir>/share/postgresql/extension` directory and run this as superuser in particular database:

    select sqlj.install_jar('file://<some_path>/gson-2.2.1.jar', 'gson', false);
    select sqlj.install_jar('file://<some_path>/postgresql-json-accessors-1.2.jar', 'json_accessors_java', false);
    select sqlj.set_classpath('public', 'gson:json_accessors_java');

After that, in any OS, reconnect as normal user (it's nesessary to apply classpath changes) and create extension
(it creates all the stored functions):

    create extension json_accessors_java

To drop all functions use:

    drop extension json_accessors_java cascade

To uninstall extension completely you may use this command (as superuser) in linux/unix:

    json_accessors_java.sh -d <postgresql_install_dir> -u

On windows - delete all copied files manually.

Using Java extension with PostgreSQL 9.0 and lower
--------------------------------------------------

Java stored procedures doesn't require extension packaging. After setting classpath all functions may be created
manually from [this file](https://github.com/alx3apps/postgresql-json-accessors/blob/master/src/main/conf/json_accessors_java--1.2.sql)

On uninstallation each function must be dropped separately.

Building and installing C extension
-----------------------------------

C extension may be built and installed using [PGXS](http://www.postgresql.org/docs/9.1/static/extend-pgxs.html).

Compilation:

    make PG_CONFIG=<postgresql_install_dir>/bin/pg_config

Compilations command also can be run from maven:

    mvn exec:exec -Dpostgresql.pg_config=<postgresql_install_dir>/bin/pg_config

Installation (as superuser):

    make PG_CONFIG=<postgresql_install_dir>/bin/pg_config install

PostgreSQL server must be restarted and extension created in particular database as superuser:

    create extension json_accessors_c

To drop all functions use:

    drop extension json_accessors_c cascade

To uninstall extension completely you may use this command (as superuser):

    make PG_CONFIG=<postgresql_install_dir>/bin/pg_config uninstall

License information
-------------------

You can use any code from this project under the terms of [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).