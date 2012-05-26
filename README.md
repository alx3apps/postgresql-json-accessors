JSON functions for PostgreSQL
=============================

[PostgreSQL](http://www.postgresql.org/) stored functions for accessing [JSON](http://www.json.org/) object and array fields. May be used for indexing JSON data.

Written in two variants (with same interfaces):

 - [PL/pgSQL](http://www.postgresql.org/docs/9.0/static/plpgsql.html) functions, written using simple regular expressions, are able to parse only simple one level JSON arrays and objects, __NOT__ support full JSON specification
 - [PL/Java](http://pgfoundry.org/projects/pljava/) functions, written on top of [google-gson](http://code.google.com/p/google-gson/) JSON parser

Third native C implementation on top of [cJSON](http://sourceforge.net/projects/cjson/) library is planning.

Indexing JSON in Postgre
------------------------

Sometimes JSON is very convenient for storing structured data in single text field. But it's not easy task to provide fast (indexed) search on separate JSON fields.
PostgreSQL have had no JSON support until version 9.2, which [introduced some support](http://www.postgresql.org/docs/9.2/static/functions-json.html).
These 9.2 functions won't help with indexing JSON data.

To create JSON index we need function, that can return JSON object field value on input JSON string and field name.
Such function may be written using [plv8js](http://code.google.com/p/plv8js/wiki/PLV8) module, [this article](http://people.planetpostgresql.org/andrew/index.php?/archives/249-Using-PLV8-to-index-JSON.html)
has an example of plv8 usage.

This project tries to provided stored functions for indexing JSON without using plv8.

Functions
---------

Main function, that gets JSON string and field name for input and returns field value as text:

    function json_object_get_text(text, text) returns text

Additional functions for other data types:

    function json_object_get_boolean(text, text) returns boolean
    function json_object_get_int(text, text) returns int
    function json_object_get_bigint(text, text) returns bigint
    function json_object_get_double(text, text) returns double precision

Similar functions for JSON arrays (indexes starts from 0):

    function json_array_get_text(text, int) returns text
    function json_array_get_boolean(text, int) returns boolean
    function json_array_get_int(text, int) returns int
    function json_array_get_bigint(text, int) returns bigint
    function json_array_get_double(text, int) returns double precision

Simple usage example, returns 42:

    select json_array_get_int('["foo", true, 42]', 2)

Complex JSON example, not supported by plpgsql functions, returns "ee":

    select json_array_get_text(json_object_get_text('{"foo" : "qq", "bar" : ["ww", "ee", "rr"]}', 'bar'), 1)

JSON index example
------------------

This example was tested on Postgre 9.0 with both pljava and plpgsql functions (JSON is deliberately simple).

Create table:

    create table test_table(id bigserial, val text);

Insert data (1572867 rows):

    insert into test_table(val) values('{"foo":"qq", "bar": true, "baz": 42, "boo": 42.424242}');
    insert into test_table(val) values('{"foo":"qq1", "bar": false, "baz": 43, "boo": 43.434343}');
    insert into test_table(val) values('{"foo":"qq2", "bar": true, "baz": 44, "boo": 44.444444}');
    insert into test_table(val) select val from test_table;
    -- repeat previous row 18 times more
    ....
    insert into test_table(val) values('{"foo":"qq3", "bar": false, "baz": 45, "boo": 45.454545}');
    insert into test_table(val) values('{"foo":"qq3", "bar": false, "baz": 45, "boo": 45.454545}');
    insert into test_table(val) values('{"foo":"qq3", "bar": false, "baz": 45, "boo": 45.454545}');

Try to query three rows, that have "qq3" value in "foo" field (query is slow):

    select * from test_table where json_object_get_text(val, 'foo') = 'qq3'

It's plan looks like:

    Seq Scan on test_table  (cost=0.00..431167.59 rows=7864 width=63) (actual time=19567.847..19567.882 rows=3 loops=1)
      Filter: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
    Total runtime: 19568.007 ms

Create JSON index:

    create index test_table_val_foo_idx on test_table(json_object_get_text(val, 'foo'));

Repeat query, now it's fast:

    select * from test_table where json_object_get_text(val, 'foo') = 'qq3'

And plan now looks like:

    Bitmap Heap Scan on test_table  (cost=149.62..16553.50 rows=7864 width=63) (actual time=0.025..0.028 rows=3 loops=1)
      Recheck Cond: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
      ->  Bitmap Index Scan on test_table_val_foo_idx  (cost=0.00..147.65 rows=7864 width=0) (actual time=0.019..0.019 rows=3 loops=1)
            Index Cond: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
    Total runtime: 0.054 ms


Building and installing
-----------------------

[plpgsql functions](https://github.com/alx3apps/postgre-json-functions/blob/master/src/main/plpgsql/postgre_json.sql) may be created by non-admin user.

pljava functions depends on gson 2.2.1 (available in maven central) and [https://github.com/alx3apps/jgit-buildnumber](jgit-buildnumber) for building.

Build maven project:

    mvn clean install

Install pljava [on linux](http://wiki.tada.se/index.php?title=Installing_on_Linux_%28or_other_*nix%29) or [on windows](http://wiki.tada.se/index.php?title=Windows_Installation).
Some pljava docs are also available [here](http://cvs.pgfoundry.org/cgi-bin/cvsweb.cgi/pljava/org.postgresql.pljava/docs/userguide.html?rev=1.15).

Run this as superuser in particular database:

    select sqlj.install_jar('file://<some_path>/gson-2.2.1.jar', 'gson_221', false)
    select sqlj.install_jar('file://<some_path>/postgre-json-functions-1.0.jar', 'postgre_json_functions_10', false)
    select sqlj.set_classpath('public', 'gson_221:postgre_json_functions_10');

Create functions [from here](https://github.com/alx3apps/postgre-json-functions/blob/master/src/main/java/ru/concerteza/postgrejson/java_json.sql#L9) as normal user (you need to reconnect on classpath update), e.g.:

    create or replace function json_object_get_text(text, text) returns text as $$
        ru.concerteza.postgrejson.JsonUtils.jsonMapGetString(java.lang.String, java.lang.String)
    $$ language java immutable;

License information
-------------------

You can use any code from this project under the terms of [Apache License 2.0.](http://www.apache.org/licenses/LICENSE-2.0).