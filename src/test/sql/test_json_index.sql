-- JSON index usage example, see
-- http://people.planetpostgresql.org/andrew/index.php?/archives/249-Using-PLV8-to-index-JSON.html
-- create table
drop table if exists test_table;
create table test_table(id bigserial, val text);

-- insert data (1572867 rows)
insert into test_table(val) values('{"foo":"qq", "bar": true, "baz": 42, "boo": 42.424242}');
insert into test_table(val) values('{"foo":"qq1", "bar": false, "baz": 43, "boo": 43.434343}');
insert into test_table(val) values('{"foo":"qq2", "bar": true, "baz": 44, "boo": 44.444444}');
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) select val from test_table;
insert into test_table(val) values('{"foo":"qq3", "bar": false, "baz": 45, "boo": 45.454545}');
insert into test_table(val) values('{"foo":"qq3", "bar": false, "baz": 45, "boo": 45.454545}');
insert into test_table(val) values('{"foo":"qq3", "bar": false, "baz": 45, "boo": 45.454545}');

-- slow query
select * from test_table where json_object_get_text(val, 'foo') = 'qq3';
-- Seq Scan on test_table  (cost=0.00..431167.59 rows=7864 width=63) (actual time=19567.847..19567.882 rows=3 loops=1)
--   Filter: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
-- Total runtime: 19568.007 ms

-- create JSON index
drop index if exists test_table_val_foo_idx;
create index test_table_val_foo_idx on test_table(json_object_get_text(val, 'foo'));

-- fast query
select * from test_table where json_object_get_text(val, 'foo') = 'qq3';
-- Bitmap Heap Scan on test_table  (cost=149.62..16553.50 rows=7864 width=63) (actual time=0.025..0.028 rows=3 loops=1)
--   Recheck Cond: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
--   ->  Bitmap Index Scan on test_table_val_foo_idx  (cost=0.00..147.65 rows=7864 width=0) (actual time=0.019..0.019 rows=3 loops=1)
--         Index Cond: (json_object_get_text(val, 'foo'::text) = 'qq3'::text)
-- Total runtime: 0.054 ms