package com.swtxml.util.injector;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.swtxml.util.properties.SimpleTypeConverters;

public class BaseConvertersTest {

	@Test
	public void testSimpleTypeConverters() {
		assertTrue(Arrays.equals(new int[] { 1, 2, 3 },
				new SimpleTypeConverters.IntArrayConverter().convert(null, "1,2,3")));
	}
}
