package com.swtxml.util.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SplitterTest {

	@Test
	public void testSplitter() {
		Splitter s = new Splitter(".,");
		assertArrayEquals(new String[] { "1", "2", "3", "4" }, s.split("1.2,3.4"));
		assertEquals("1.2.3.4", s.join(s.split("1.2,3.4")));
		assertEquals(true, s.isSeparator('.'));
		assertEquals(true, s.isSeparator(','));
		assertEquals(false, s.isSeparator('x'));
	}

}
