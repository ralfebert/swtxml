/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.swt.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.types.StyleType;

public class SwtNamespaceTest {

	private SwtNamespace swt;
	private WidgetTag buttonTag;
	private WidgetTag compositeTag;

	@Before
	public void setUp() throws Exception {
		swt = SwtInfo.NAMESPACE;
		buttonTag = swt.getTag("Button");
		compositeTag = swt.getTag("Composite");
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

	@Test
	public void testScoping() {
		assertTrue(compositeTag.isAllowedIn(ITagDefinition.ROOT));
		assertFalse(buttonTag.isAllowedIn(ITagDefinition.ROOT));
		assertFalse(compositeTag.isAllowedIn(buttonTag));
		assertTrue(buttonTag.isAllowedIn(compositeTag));
		assertFalse(buttonTag.isAllowedIn(buttonTag));
		assertTrue(compositeTag.isAllowedIn(compositeTag));
		assertTrue(swt.getTag("Button").isAllowedIn(swt.getTag("TabItem")));
		assertTrue(swt.getTag("TabItem").isAllowedIn(swt.getTag("TabFolder")));
		assertTrue(swt.getTag("TabFolder").isAllowedIn(swt.getTag("TabItem")));
		assertFalse(swt.getTag("Button").isAllowedIn(swt.getTag("TabFolder")));
		assertTrue(swt.getTag("Tree").isAllowedIn(swt.getTag("Group")));
	}

	@Test
	public void testTagsWithoutStyleFlagsHaveNoStyleAttribute() {
		assertNull(swt.getTag("TabItem").getAttribute("style"));
	}
}
