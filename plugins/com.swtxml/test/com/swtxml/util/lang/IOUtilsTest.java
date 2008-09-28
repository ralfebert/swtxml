package com.swtxml.util.lang;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class IOUtilsTest {

	@Test
	public void testToStringInputStream() {
		String text = IOUtils.toString(getClass().getResourceAsStream("sometext.txt"));
		assertEquals(StringUtils.repeat(StringUtils.repeat("10", 56) + "\n", 142).trim(), text);
	}
}
