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
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.junit.Before;
import org.junit.Test;

public class WidgetRegistryTest {

	private WidgetRegistry registry;

	@Before
	public void setup() {
		registry = new WidgetRegistry();
	}

	@Test
	public void testGetWidgetClassNames() {
		assertTrue(registry.getWidgetClassNames().contains(Button.class.getName()));
		assertTrue(registry.getWidgetClassNames().contains(Composite.class.getName()));
	}

	@Test
	public void testGetAllowedStylesFor() {
		Collection<String> buttonStyles = registry.getAllowedStylesFor(Button.class);
		assertTrue(buttonStyles.toString(), buttonStyles.contains("PUSH"));
		assertTrue(buttonStyles.toString(), buttonStyles.contains("LEFT_TO_RIGHT"));
		assertFalse(buttonStyles.toString(), buttonStyles.contains("COLOR_RED"));
	}

	@Test
	public void testGetWidgetClass() {
		assertEquals(Button.class, registry.getWidgetClass(Button.class.getName()));
	}

	@Test
	public void testGetAllowedParentType() {
		assertEquals(Composite.class, registry.getAllowedParentType(Button.class));
		assertEquals(Table.class, registry.getAllowedParentType(TableItem.class));
	}
}
