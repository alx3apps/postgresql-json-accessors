-- http://cvs.pgfoundry.org/cgi-bin/cvsweb.cgi/pljava/org.postgresql.pljava/docs/userguide.html?rev=1.15
-- as superuser
select sqlj.install_jar('file://<some_path>/gson-2.2.1.jar', 'gson_221', false)
-- select sqlj.remove_jar('gson_221', false)
select sqlj.install_jar('file://<some_path>/postgre-json-functions-1.0.jar', 'postgre_json_functions_10', false)
-- select sqlj.remove_jar('postgre_json_functions_10', false)
select sqlj.set_classpath('public', 'gson_221:postgre_json_functions_10');

-- as normal user
create or replace function json_object_get_text(text, text) returns text as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetString(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_object_get_boolean(text, text) returns boolean as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetBoolean(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_object_get_int(text, text) returns int as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetInteger(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_object_get_bigint(text, text) returns bigint as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetLong(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_object_get_numeric(text, text) returns numeric as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetBigDecimal(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_object_get_timestamp(text, text) returns timestamp as $$
    ru.concerteza.postgrejson.JsonUtils.jsonMapGetTimestamp(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_text_array(text) returns text[] as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayToStringArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_boolean_array(text) returns boolean[] as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayToBooleanArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_int_array(text) returns int[] as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayToIntegerArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_bigint_array(text) returns bigint[] as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayToLongArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_numeric_array(text) returns numeric[] as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayToBigDecimalArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_timestamp_array(text) returns timestamp without time zone[] as $$
    ru.concerteza.postgrejson.JsonUtils.jsonArrayToTimestampArray(java.lang.String)
$$ language java immutable returns null on null input;
