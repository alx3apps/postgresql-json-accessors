JSON functions for PostgreSQL
=============================

[PostgreSQL](http://www.postgresql.org/) stored functions for accessing [JSON](http://www.json.org/) fields.

If you have text (varchar) columns with data like this:

    {"create_date":"2009-12-01 01:23:45","tags":["foo","bar","baz"]}

these functions can be used for:

 - creating B-tree (default) indexes on JSON object fields (`create_date` field)
 - creating [GIN](http://www.postgresql.org/docs/9.0/static/gin.html) indexes on JSON arrays (`tags` field)

Functions are written using [PL/Java](http://pgfoundry.org/projects/pljava/), on top of [google-gson](http://code.google.com/p/google-gson/)
JSON parser. Native C implementation on top of [cJSON](http://sourceforge.net/projects/cjson/) library is in our plans.

Indexing JSON in Postgre
------------------------

Sometimes JSON is very convenient for storing structured data in single text field. But it's not easy to provide fast (indexed) search on separate JSON fields.
PostgreSQL have had no JSON support until version 9.2, which [introduced some support](http://www.postgresql.org/docs/9.2/static/functions-json.html).
These 9.2 functions won't help with indexing JSON data.

To create JSON index we need stored functions to parse JSON text fields.
Such function may be written using [plv8js](http://code.google.com/p/plv8js/wiki/PLV8) module, [this article](http://people.planetpostgresql.org/andrew/index.php?/archives/249-Using-PLV8-to-index-JSON.html)
has an example of plv8 usage.

This project provides stored functions for indexing JSON without using plv8.

Functions
---------

__Function for accessing JSON object fields:__

    function json_object_get_text(text, text) returns text

Usage example, returns `qq`:

    select json_object_get_text('{"foo":"qq", "bar": true}', 'foo')

There are also similar functions returning `boolean`, `int`, `bigint`, `numeric` and `timestamp without timezone`.
Timestamp format `yyyy-MM-dd HH:mm:ss` is fixed, due to `Timestamp.valueOf(...)` usage for parsing dates.

__Function for converting JSON arrays into postgre arrays:__

    function json_array_to_text_array(text) returns text[]

Usage example, returns `array['foo', 'bar']`:

    select json_array_to_text_array('["foo", "bar"]')

There are also similar functions returning `boolean[]`, `int[]`, `bigint[]`, `numeric[]` and `timestamp without timezone[]`.
All primitive arrays returns from Java functions in boxed form (`Boolean[]` etc.) to allow returning `NULL` elements.
Having nulls in such arrays is not a good idea, but "Cannot assign null to int" errors in stored functions are worse.

JSON index examples
-------------------

This example was tested on Postgre 9.0, full example is [here](https://github.com/alx3apps/postgre-json-functions/blob/master/src/test/sql/test_json_index.sql)

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

    select * from test_table where json_object_get_timestamp(val, 'create_date')
        between '2009-12-04 01:00:00' and '2009-12-04 02:00:00';

It's plan looks like:

    Seq Scan on test_table  (cost=0.00..830998.51 rows=7864 width=76) (actual time=21904.068..21904.124 rows=3 loops=1)
      Filter: ((json_object_get_timestamp(val, 'create_date'::text) >= '2009-12-04 01:00:00'::timestamp without time zone) AND (json_object_get_timestamp(val, 'create_date'::text) <= '2009-12-04 02:00:00'::timestamp without time zone))
    Total runtime: 21904.157 ms

Create B-tree index on `create_date` JSON field:

    create index test_date_idx on test_table using btree (json_object_get_timestamp(val, 'create_date'));

Repeat query, now it's fast and plan looks like:

    Bitmap Heap Scan on test_table  (cost=149.62..16553.50 rows=7864 width=63) (actual time=0.025..0.028 rows=3 loops=1)
      Recheck Cond: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
      ->  Bitmap Index Scan on test_table_val_foo_idx  (cost=0.00..147.65 rows=7864 width=0) (actual time=0.019..0.019 rows=3 loops=1)
            Index Cond: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
    Total runtime: 0.054 ms

Try to query three rows, inserted last, checking inclusion into `tags` JSON field (query is slow):

    select * from test_table where json_array_to_text_array(json_object_get_text(val, 'tags')) @> array['bar4'];

It's plan looks like:

    Seq Scan on test_table  (cost=0.00..827066.34 rows=1573 width=76) (actual time=41336.691..41336.749 rows=3 loops=1)
      Filter: (json_array_to_text_array(json_object_get_text(val, 'tags'::text)) @> '{bar4}'::text[])
    Total runtime: 41336.799 ms

Create GIN index on `tags` JSON field:

    create index test_tags_idx on test_table using gin (json_array_to_text_array(json_object_get_text(val, 'tags')));

Repeat query, now it's fast and plan looks like:

    Bitmap Heap Scan on test_table  (cost=29.08..5679.25 rows=1573 width=76) (actual time=0.050..0.055 rows=3 loops=1)
      Recheck Cond: (json_array_to_text_array(json_object_get_text(val, 'tags'::text)) @> '{bar4}'::text[])
      ->  Bitmap Index Scan on test_tags_idx  (cost=0.00..28.69 rows=1573 width=0) (actual time=0.037..0.037 rows=3 loops=1)
            Index Cond: (json_array_to_text_array(json_object_get_text(val, 'tags'::text)) @> '{bar4}'::text[])
    Total runtime: 0.129 ms

Building and installing
-----------------------

Project depends on gson 2.2.1 (available in maven central) and [jgit-buildnumber](https://github.com/alx3apps/jgit-buildnumber) for building.

Build maven project:

    mvn clean install

Install pljava [on linux](http://wiki.tada.se/index.php?title=Installing_on_Linux_%28or_other_*nix%29) or [on windows](http://wiki.tada.se/index.php?title=Windows_Installation).
Some pljava docs are also available [here](http://cvs.pgfoundry.org/cgi-bin/cvsweb.cgi/pljava/org.postgresql.pljava/docs/userguide.html?rev=1.15).

Run this as superuser in particular database:

    select sqlj.install_jar('file://<some_path>/gson-2.2.1.jar', 'gson_221', false);
    select sqlj.install_jar('file://<some_path>/postgre-json-functions-1.1.jar', 'postgre_json_functions_11', false);
    select sqlj.set_classpath('public', 'gson_221:postgre_json_functions_11');

Create functions [from here](https://github.com/alx3apps/postgre-json-functions/blob/master/src/main/java/ru/concerteza/postgrejson/java_json.sql#L9)
as normal user (you need to reconnect on classpath update), e.g.:

    create or replace function json_object_get_text(text, text) returns text as $$
        ru.concerteza.postgrejson.JsonUtils.jsonMapGetString(java.lang.String, java.lang.String)
    $$ language java immutable returns null on null input;

License information
-------------------

You can use any code from this project under the terms of [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).