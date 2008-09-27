package com.swtxml.util.properties;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.swtxml.util.types.SimpleTypes;

public class BaseConvertersTest {

	@Test
	public void testSimpleTypeConverters() {
		assertTrue(Arrays.equals(new int[] { 1, 2, 3 },
				new SimpleTypes.IntArrayConverter().convert("1,2,3")));
	}
}
