/* json_accessors_java--1.2.sql */
-- http://wiki.tada.se/index.php?title=Installing_on_Linux_%28or_other_*nix%29
-- http://wiki.tada.se/index.php?title=Windows_Installation
-- http://cvs.pgfoundry.org/cgi-bin/cvsweb.cgi/pljava/org.postgresql.pljava/docs/userguide.html?rev=1.15

-- complain if script is sourced in psql, rather than via CREATE EXTENSION
\echo Use "create extension json_accessors_java" to load this file. \quit

-- as normal user

-- primitive getters

create or replace function json_get_object(text, text) returns text as $$
    ru.concerteza.postgresql.JsonAccessors.getObject(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_text(text, text) returns text as $$
    ru.concerteza.postgresql.JsonAccessors.getString(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_boolean(text, text) returns boolean as $$
    ru.concerteza.postgresql.JsonAccessors.getBoolean(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_int(text, text) returns int as $$
    ru.concerteza.postgresql.JsonAccessors.getInteger(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_bigint(text, text) returns bigint as $$
    ru.concerteza.postgresql.JsonAccessors.getLong(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_numeric(text, text) returns numeric as $$
    ru.concerteza.postgresql.JsonAccessors.getBigDecimal(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_timestamp(text, text) returns timestamp as $$
    ru.concerteza.postgresql.JsonAccessors.getTimestamp(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

-- array getters

create or replace function json_get_object_array(text, text) returns text[] as $$
    ru.concerteza.postgresql.JsonAccessors.getObjectArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_multi_array(text, text) returns text[] as $$
    ru.concerteza.postgresql.JsonAccessors.getMultiArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_text_array(text, text) returns text[] as $$
    ru.concerteza.postgresql.JsonAccessors.getStringArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_boolean_array(text, text) returns boolean[] as $$
    ru.concerteza.postgresql.JsonAccessors.getBooleanArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_int_array(text, text) returns int[] as $$
    ru.concerteza.postgresql.JsonAccessors.getIntegerArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_bigint_array(text, text) returns bigint[] as $$
    ru.concerteza.postgresql.JsonAccessors.getLongArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_numeric_array(text, text) returns numeric[] as $$
    ru.concerteza.postgresql.JsonAccessors.getBigDecimalArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_get_timestamp_array(text, text) returns timestamp[] as $$
    ru.concerteza.postgresql.JsonAccessors.getTimestampArray(java.lang.String, java.lang.String)
$$ language java immutable returns null on null input;

-- array conversion

create or replace function json_array_to_object_array(text) returns text[] as $$
    ru.concerteza.postgresql.JsonAccessors.toObjectArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_multi_array(text) returns text[] as $$
    ru.concerteza.postgresql.JsonAccessors.toMultiArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_text_array(text) returns text[] as $$
    ru.concerteza.postgresql.JsonAccessors.toStringArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_boolean_array(text) returns boolean[] as $$
    ru.concerteza.postgresql.JsonAccessors.toBooleanArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_int_array(text) returns int[] as $$
    ru.concerteza.postgresql.JsonAccessors.toIntegerArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_bigint_array(text) returns bigint[] as $$
    ru.concerteza.postgresql.JsonAccessors.toLongArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_numeric_array(text) returns numeric[] as $$
    ru.concerteza.postgresql.JsonAccessors.toBigDecimalArray(java.lang.String)
$$ language java immutable returns null on null input;

create or replace function json_array_to_timestamp_array(text) returns timestamp[] as $$
    ru.concerteza.postgresql.JsonAccessors.toTimestampArray(java.lang.String)
$$ language java immutable returns null on null input;
