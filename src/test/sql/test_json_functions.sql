-- must return true

select 'qq' = json_object_get_text('{"foo":"qq", "bar": true}', 'foo')
    and true = json_object_get_boolean('{"foo":"qq", "bar": true}', 'bar')
    and 42 = json_object_get_int('{"baz": 42, "boo": 42.424242}', 'baz')
    and 42 = json_object_get_bigint('{"baz": 42, "boo": 42.424242}', 'baz')
    and 42.424242 = json_object_get_numeric('{"baz": 42, "boo": 42.424242}', 'boo')
    and cast('2009-12-01 01:23:45' as timestamp without time zone) = json_object_get_timestamp('{"foo":"qq", "bar": "2009-12-01 01:23:45"}', 'bar')
    and array['foo','bar'] = json_array_to_text_array('["foo", "bar"]')
    and array[true,false] = json_array_to_boolean_array('[true, false]')
    and array[42,43] = json_array_to_int_array('[42, 43]')
    and array[cast(42 as bigint), cast(43 as bigint)] = json_array_to_bigint_array('[42, 43]')
    and array[42.4242, 43.4343] = json_array_to_numeric_array('[42.4242, 43.4343]')
    and array[cast('2009-12-01 01:23:45' as timestamp without time zone)] = json_array_to_timestamp_array('["2009-12-01 01:23:45"]')