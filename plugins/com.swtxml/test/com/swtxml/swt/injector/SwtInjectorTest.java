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
package com.swtxml.swt.injector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.junit.Test;

import com.swtxml.i18n.LabelType;
import com.swtxml.swt.SwtInfo;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.IInjector;

public class SwtInjectorTest {

	@Test
	public void testSwtLayout() {
		Composite composite = new Composite(new Shell(), SWT.NONE);
		IInjector injector = SwtInfo.WIDGET_PROPERTIES.getProperties(composite.getClass())
				.getInjector(composite);
		injector.setPropertyValue("layout", "layout:grid;numColumns:2;");
		assertEquals(2, ((GridLayout) composite.getLayout()).numColumns);
	}

	@Test
	public void testStringPropertiesAreLabels() {
		ClassProperties<Text> properties = SwtInfo.WIDGET_PROPERTIES.getProperties(Text.class);
		assertTrue(properties.getProperties().get("text").getType() instanceof LabelType);
	}

}
