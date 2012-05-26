-- http://cvs.pgfoundry.org/cgi-bin/cvsweb.cgi/pljava/org.postgresql.pljava/docs/userguide.html?rev=1.15
-- as superuser
select sqlj.install_jar('file://<some_path>/gson-2.2.1.jar', 'gson_221', false)
-- select sqlj.remove_jar('gson_221', false)
select sqlj.install_jar('file://<some_path>/postgre-json-functions-1.0.jar', 'postgre_json_functions_10', false)
-- select sqlj.remove_jar('postgre_json_functions_10', false)
select sqlj.set_classpath('public', 'gson_221:postgre_json_functions_10');

-- as normal user
create or replace function json_array_get_text(text, int) returns text as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayGetString(java.lang.String, int)
$$ language java immutable;

create or replace function json_array_get_boolean(text, int) returns boolean as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayGetBoolean(java.lang.String, int)
$$ language java immutable;

create or replace function json_array_get_int(text, int) returns int as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayGetInteger(java.lang.String, int)
$$ language java immutable;

create or replace function json_array_get_bigint(text, int) returns bigint as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayGetLong(java.lang.String, int)
$$ language java immutable;

create or replace function json_array_get_double(text, int) returns double precision as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayGetDouble(java.lang.String, int)
$$ language java immutable;

create or replace function json_object_get_text(text, text) returns text as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetString(java.lang.String, java.lang.String)
$$ language java immutable;

create or replace function json_object_get_boolean(text, text) returns boolean as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetBoolean(java.lang.String, java.lang.String)
$$ language java immutable;

create or replace function json_object_get_int(text, text) returns int as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetInteger(java.lang.String, java.lang.String)
$$ language java immutable;

create or replace function json_object_get_bigint(text, text) returns bigint as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetLong(java.lang.String, java.lang.String)
$$ language java immutable;

create or replace function json_object_get_double(text, text) returns double precision as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetDouble(java.lang.String, java.lang.String)
$$ language java immutable;