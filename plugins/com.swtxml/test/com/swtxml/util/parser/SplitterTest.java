package com.swtxml.util.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SplitterTest {

	@Test
	public void testNone() {
		Splitter rule = Splitter.allowMultiple('.', ".,");
		assertArrayEquals(new String[] { "1", "2", "3", "4" }, rule.split("1.2,3.4"));
		assertEquals("1.2.3.4", rule.join(rule.split("1.2,3.4")));
	}

	@Test
	public void testAllowMultiple() {
		Splitter rule = Splitter.none();
		assertArrayEquals(new String[] { "1.2,3.4" }, rule.split("1.2,3.4"));
		assertEquals("1.2,3.4", rule.join(rule.split("1.2,3.4")));
	}

}
