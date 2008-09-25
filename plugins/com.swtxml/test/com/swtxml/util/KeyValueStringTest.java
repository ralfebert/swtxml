package com.swtxml.util;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class KeyValueStringTest {

	@Test
	public void testParse() throws Exception {
		Map<String, String> values = KeyValueString.parse(" test:xyz;  abc:def ;");
		assertEquals("xyz", values.get("test"));
		assertEquals("def", values.get("abc"));
		assertEquals(2, values.size());
	}

}
