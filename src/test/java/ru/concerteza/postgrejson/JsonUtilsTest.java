package ru.concerteza.postgrejson;

import com.google.gson.JsonSyntaxException;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static ru.concerteza.postgrejson.JsonUtils.*;

/**
 * User: alexey
 * Date: 5/26/12
 */
public class JsonUtilsTest {
    @Test(expected = JsonSyntaxException.class)
    public void testJsonArrayGetJSE() {
        jsonMapGetString("[\"foo\",\"bar\",\"baz\"]]]", "foo");
    }

    @Test
    public void testJsonMapGet() {
        assertEquals("String fail", "qq", jsonMapGetString("{\"foo\":\"qq\",\"bar\":true}", "foo"));
        assertEquals("Boolean fail", true, jsonMapGetBoolean("{\"foo\":\"qq\",\"bar\":true}", "bar"));
        assertEquals("Integer fail", 42, (int) jsonMapGetInteger("{\"bar\":true,\"baz\":42}", "baz"));
        assertEquals("Long fail", 42L, (long) jsonMapGetLong("{\"bar\":true,\"baz\":42}", "baz"));
        assertEquals("BigDecimal fail", new BigDecimal("42.4242"), jsonMapGetBigDecimal("{\"bar\":true,\"baz\":42.4242}", "baz"));
        assertEquals("Timestamp fail", new Timestamp(new GregorianCalendar(2012, Calendar.JANUARY, 2, 3, 45, 56).getTimeInMillis()),
                jsonMapGetTimestamp("{\"bar\":true,\"baz\": \"2012-01-02 03:45:56\"}", "baz"));
        assertNull("Null fail", jsonMapGetLong("{\"foo\":\"qq\"}", "42"));
    }

    @Test
    public void testArrayJsonToArray() {
        assertArrayEquals("String fail", new String[]{"foo", null, "bar"}, jsonArrayToStringArray("[\"foo\", null, \"bar\"]"));
        assertArrayEquals("Boolean fail", new Boolean[]{true, null, false}, jsonArrayToBooleanArray("[true, null, false]"));
        assertArrayEquals("Integer fail", new Integer[]{42, 43, null}, jsonArrayToIntegerArray("[42, 43, null]"));
        assertArrayEquals("Long fail", new Long[]{42L, 43L, null}, jsonArrayToLongArray("[42, 43, null]"));
        assertArrayEquals("BigDecimal fail", new BigDecimal[]{new BigDecimal("42.4242"), new BigDecimal("43.4343")},
                jsonArrayToBigDecimalArray("[42.4242, 43.4343]"));
        assertArrayEquals("Timestamp fail", new String[]{"foo", "bar"}, jsonArrayToStringArray("[\"foo\", \"bar\"]"));
        assertArrayEquals("String fail", new Timestamp[]{new Timestamp(new GregorianCalendar(2012, Calendar.JANUARY, 2, 3, 45, 56).getTimeInMillis()), null},
                jsonArrayToTimestampArray("[\"2012-01-02 03:45:56\", null]"));
    }
}
