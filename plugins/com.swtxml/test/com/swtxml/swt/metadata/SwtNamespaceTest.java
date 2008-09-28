package com.swtxml.swt.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.types.StyleType;

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

		assertNull("abstract classes cannot be used as tag", swt.getTag("Control"));
		assertNull("abstract classes cannot be used as tag", swt.getTag("Widget"));
	}

	@Test
	public void testWidgetStylesRestricted() {
		IAttributeDefinition style = buttonTag.getAttribute("style");
		StyleType type = (StyleType) style.getType();
		assertTrue(type.getAllowedStyles().contains("TOGGLE"));
		assertFalse(type.getAllowedStyles().contains("COLOR_RED"));
	}
}
