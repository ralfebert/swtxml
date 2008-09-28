package com.swtxml.swt.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.swt.SwtInfo;

public class SwtNamespaceTest {

	private SwtNamespace swt;
	private WidgetTag buttonTag;

	@Before
	public void setUp() throws Exception {
		swt = SwtInfo.NAMESPACE;
		buttonTag = swt.getTag("Button");
	}

	@Test
	public void testGetTagMetaData() {
		assertEquals("Button", buttonTag.getName());

		assertTrue(swt.getTagNames().size() > 0);
		assertTrue(buttonTag.getAttributeNames().size() > 0);

		IAttributeDefinition textAttribute = buttonTag.getAttribute("text");
		assertEquals("text", textAttribute.getName());
	}

}
