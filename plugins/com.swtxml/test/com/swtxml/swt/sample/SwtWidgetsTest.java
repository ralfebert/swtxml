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
package com.swtxml.swt.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.resources.ClassResource;
import com.swtxml.swt.SwtXmlParser;

public class SwtWidgetsTest {

	private static final String SAMPLE_SWTXML = "SwtWidgetsExamplesWindow.swtxml";
	private SwtWidgetsExamplesWindow window;

	@Before
	public void setupShell() {
		window = new SwtWidgetsExamplesWindow(null);
		window.create();
	}

	@Test
	public void testWidgetHierarchy() {
		assertWindowHierarchy((Composite) window.getShell().getChildren()[0]);
	}

	@Test
	public void testParseFromStream() {
		Shell parent = new Shell();
		SwtXmlParser parser = new SwtXmlParser(parent,
				new ClassResource(getClass(), SAMPLE_SWTXML), null);
		parser.parse();
		assertWindowHierarchy(parent);
	}

	@Test
	public void testParseByColocatedXml() {
		Shell parent = new Shell();
		SwtXmlParser parser = new SwtXmlParser(parent, ClassResource.coLocated(
				SwtWidgetsExamplesWindow.class, "swtxml"), null);
		parser.parse();
		assertWindowHierarchy(parent);
	}

	private void assertWindowHierarchy(Composite rootComposite) {
		TabFolder tabFolder = (TabFolder) rootComposite.getChildren()[0];
		TabItem tabItem1 = tabFolder.getItems()[0];
		assertEquals("RowLayout", tabItem1.getText());
		Composite tabItem1Composite = (Composite) tabItem1.getControl();
		RowLayout tabItem1CompositeLayout = (RowLayout) tabItem1Composite.getLayout();
		assertEquals(SWT.VERTICAL, tabItem1CompositeLayout.type);
		assertEquals(5, tabItem1CompositeLayout.spacing);
	}

	@Test
	public void testImage() {
		Image image = window.getSomeImageLabel().getImage();
		assertNotNull(image);
		window.getShell().dispose();
		assertTrue(image.isDisposed());
	}

	@Test
	public void testInjectionById() {
		assertEquals("Sauber", window.getTestButton().getText());
	}
}
