package com.swtxml.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SwtTagRegistryTest {

	private SwtTagRegistry swtTagRegistry;

	@Before
	public void setUp() throws Exception {
		swtTagRegistry = new SwtTagRegistry();
	}

	@Test
	public void testGetTagMetaData() {
		ITag tag = swtTagRegistry.getTag("Button");
		assertEquals("Button", tag.getName());

		assertEquals(null, swtTagRegistry.getTag("wegewg"));
		assertEquals(null, tag.getAttribute("erherhe"));

		ITagAttribute textAttribute = tag.getAttribute("text");
		assertEquals("text", textAttribute.getName());

		System.out.println(tag.getAttributes());
		assertTrue(tag.getAttributes().contains(textAttribute));
	}

}
