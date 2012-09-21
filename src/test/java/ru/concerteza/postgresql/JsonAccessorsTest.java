package ru.concerteza.postgresql;

import com.google.gson.JsonSyntaxException;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static ru.concerteza.postgresql.JsonAccessors.*;

/**
 * User: alexey
 * Date: 5/26/12
 */
public class JsonAccessorsTest {
    @Test(expected = JsonSyntaxException.class)
    public void testJsonArrayGetJSE() {
        getString("[\"foo\",\"bar\",\"baz\"]]]", "foo");
    }

    @Test
    public void testJsonMapGet() {
        assertEquals("Object fail", "{\"boo\":42}", getObject("{\"foo\":{\"boo\":42},\"bar\":true}", "foo"));
        assertEquals("String fail", "qq", getString("{\"foo\":\"qq\",\"bar\":true}", "foo"));
        assertEquals("Boolean fail", true, getBoolean("{\"foo\":\"qq\",\"bar\":true}", "bar"));
        assertEquals("Integer fail", 42, (int) getInteger("{\"bar\":true,\"baz\":42}", "baz"));
        assertEquals("Long fail", 42L, (long) getLong("{\"bar\":true,\"baz\":42}", "baz"));
        assertEquals("BigDecimal fail", new BigDecimal("42.4242"), getBigDecimal("{\"bar\":true,\"baz\":42.4242}", "baz"));
        assertEquals("Timestamp fail", new Timestamp(new GregorianCalendar(2012, Calendar.JANUARY, 2, 3, 45, 56).getTimeInMillis()),
                getTimestamp("{\"bar\":true,\"baz\": \"2012-01-02 03:45:56\"}", "baz"));
        assertNull("Null fail", getLong("{\"foo\":\"qq\"}", "42"));
    }

    @Test
    public void testGetArray() {
        assertArrayEquals("Object array fail", new String[]{"{\"foo\":43}", null, "{}"}, getObjectArray("{\"boo\": [{\"foo\":43}, null, {}]}", "boo"));
        assertArrayEquals("Multi array fail", new String[]{"[42,43]", null, "[]"}, getMultiArray("{\"boo\": [[42,43], null, []]}", "boo"));
        assertArrayEquals("String array fail", new String[]{"foo", null, "bar"}, getStringArray("{\"boo\": [\"foo\", null, \"bar\"]}", "boo"));
        assertArrayEquals("Boolean array fail", new Boolean[]{true, null, false}, getBooleanArray("{\"boo\": [true, null, false]}", "boo"));
        assertArrayEquals("Integer array fail", new Integer[]{42, 43, null}, getIntegerArray("{\"boo\": [42, 43, null]}", "boo"));
        assertArrayEquals("Long array fail", new Long[]{42L, 43L, null}, getLongArray("{\"boo\": [42, 43, null]}", "boo"));
        assertArrayEquals("BigDecimal array fail", new BigDecimal[]{new BigDecimal("42.4242"), new BigDecimal("43.4343")},
                getBigDecimalArray("{\"boo\": [42.4242, 43.4343]}", "boo"));
        assertArrayEquals("Timestamp array fail", new Timestamp[]{new Timestamp(new GregorianCalendar(2012, Calendar.JANUARY, 2, 3, 45, 56).getTimeInMillis()), null},
                getTimestampArray("{\"boo\": [\"2012-01-02 03:45:56\", null]}", "boo"));
    }

    @Test
    public void testToArray() {
        assertArrayEquals("Object array fail", new String[]{"{\"foo\":43}", null, "{}"}, toObjectArray("[{\"foo\":43}, null, {}]"));
        assertArrayEquals("Multi array fail", new String[]{"[42,43]", null, "[]"}, toMultiArray("[[42,43], null, []]"));
        assertArrayEquals("String array fail", new String[]{"foo", null, "bar"}, toStringArray("[\"foo\", null, \"bar\"]"));
        assertArrayEquals("Boolean array fail", new Boolean[]{true, null, false}, toBooleanArray("[true, null, false]"));
        assertArrayEquals("Integer array fail", new Integer[]{42, 43, null}, toIntegerArray("[42, 43, null]"));
        assertArrayEquals("Long array fail", new Long[]{42L, 43L, null}, toLongArray("[42, 43, null]"));
        assertArrayEquals("BigDecimal array fail", new BigDecimal[]{new BigDecimal("42.4242"), new BigDecimal("43.4343")},
                toBigDecimalArray("[42.4242, 43.4343]"));
        assertArrayEquals("Timestamp array fail", new Timestamp[]{new Timestamp(new GregorianCalendar(2012, Calendar.JANUARY, 2, 3, 45, 56).getTimeInMillis()), null},
                toTimestampArray("[\"2012-01-02 03:45:56\", null]"));
    }
}
