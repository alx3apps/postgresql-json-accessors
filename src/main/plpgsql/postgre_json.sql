create or replace function json_array_get_text(text, int) returns text as $$
    select substring((regexp_split_to_array(substring($1 from E'^\\s*\\[\\s*(.*)\\s*\\]\\s*$'), E',\\s*'))[$2 + 1], E'^\\s*"?(.*[^"])"?\\s*$')
$$ language sql immutable;

create or replace function json_array_get_boolean(text, int) returns boolean as $$
    select cast(json_array_get_text($1, $2) as boolean)
$$ language sql immutable;

create or replace function json_array_get_int(text, int) returns int as $$
    select cast(json_array_get_text($1, $2) as int)
$$ language sql immutable;

create or replace function json_array_get_bigint(text, int) returns bigint as $$
    select cast(json_array_get_text($1, $2) as bigint)
$$ language sql immutable;

create or replace function json_array_get_double(text, int) returns double precision as $$
    select cast(json_array_get_text($1, $2) as double precision)
$$ language sql immutable;

create or replace function json_object_get_text(text, text) returns text as $$
declare
    arr text[];
    pair text[];
    i int := 1;
begin
arr := regexp_split_to_array(substring($1 from E'^\\s*{\\s*(.*)\\s*}\\s*$'), E',\\s*');
while i <= array_upper(arr, 1) loop
    pair := regexp_matches(arr[i], E'^\\s*"(.*?)"\\s*:\\s*"?(.*[^"])"?\\s*$');
    if pair[1] = $2 then
        return pair[2];
    end if;
    i := i + 1;
end loop;
return NULL;
end;
$$ language plpgsql immutable;

create or replace function json_object_get_boolean(text, text) returns boolean as $$
    select cast(json_object_get_text($1, $2) as boolean)
$$ language sql immutable;

create or replace function json_object_get_int(text, text) returns int as $$
    select cast(json_object_get_text($1, $2) as int)
$$ language sql immutable;

create or replace function json_object_get_bigint(text, text) returns bigint as $$
    select cast(json_object_get_text($1, $2) as bigint)
$$ language sql immutable;

create or replace function json_object_get_double(text, text) returns double precision as $$
    select cast(json_object_get_text($1, $2) as double precision)
$$ language sql immutable;