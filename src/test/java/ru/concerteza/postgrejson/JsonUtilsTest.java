package ru.concerteza.postgrejson;

import com.google.gson.JsonSyntaxException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static ru.concerteza.postgrejson.JsonUtils.*;

/**
 * User: alexey
 * Date: 5/26/12
 */
public class JsonUtilsTest {
    @Test
    public void testJsonArrayGet() {
        assertEquals("String fail", "foo", jsonArrayGetString("[\"foo\",true,42]", 0));
        assertEquals("Boolean fail", true, jsonArrayGetBoolean("[\"foo\",true,42]", 1));
        assertEquals("Long fail", 42L, (long) jsonArrayGetInteger("[\n\n\"foo\"\t\n,\ntrue\t\t,\n\n42]", 2));
        assertEquals("Null fail", null, jsonArrayGetDouble("[\"foo\",true,42]", 42));
    }

    @Test(expected = JsonSyntaxException.class)
    public void testJsonArrayGetJSE() {
        jsonArrayGetString("[\"foo\",\"bar\",\"baz\"]]]", 42);
    }

    @Test
    public void testJsonMapGet() {
        assertEquals("String fail", "qq", jsonMapGetString("{\"foo\":\"qq\",\"bar\":true,\"baz\":42}", "foo"));
        assertEquals("Boolean fail", true, jsonMapGetBoolean("{\"foo\":\"qq\",\"bar\":true,\"baz\":42}", "bar"));
        assertEquals("Long fail", 42L, (long) jsonMapGetInteger("{\"foo\":\"qq\",\"bar\":true,\"baz\":42}", "baz"));
        assertEquals("Null fail", null, jsonMapGetDouble("{\"foo\":\"qq\",\"bar\":true,\"baz\":42}", "42"));
    }

}
