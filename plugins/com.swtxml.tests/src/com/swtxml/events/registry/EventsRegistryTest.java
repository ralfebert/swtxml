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
package com.swtxml.events.registry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.VisibilityWindowListener;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.swt.SwtInfo;

public class EventsRegistryTest {

	private EventsRegistry eventRegistry;

	@Before
	public void createEventRegistry() {
		eventRegistry = EventsRegistry.scanWidgets(SwtInfo.WIDGETS);
	}

	@Test
	public void getAllEventNames() {
		assertTrue(eventRegistry.getAllEventNames().contains("widgetSelected"));
		assertTrue(eventRegistry.getAllEventNames().contains("focusGained"));
	}

	@Test
	public void isEventAvailableFor() {
		assertNotNull(eventRegistry.getWidgetEvent(Button.class, "widgetSelected"));
		assertNull(eventRegistry.getWidgetEvent(Control.class, "widgetSelected"));
		assertNotNull(eventRegistry.getWidgetEvent(Button.class, "focusGained"));
		assertNotNull(eventRegistry.getWidgetEvent(Control.class, "focusGained"));
	}

	@Test
	public void testGetEventInterface() {
		assertEquals(SelectionListener.class, eventRegistry.getWidgetEvent(Button.class,
				"widgetSelected").getListenerInterfaceClass());
		assertEquals(FocusListener.class, eventRegistry.getWidgetEvent(Button.class, "focusGained")
				.getListenerInterfaceClass());
		assertEquals(ModifyListener.class, eventRegistry.getWidgetEvent(Text.class, "modifyText")
				.getListenerInterfaceClass());
		assertEquals(ModifyListener.class, eventRegistry.getWidgetEvent(StyledText.class,
				"modifyText").getListenerInterfaceClass());
		assertEquals(ExtendedModifyListener.class, eventRegistry.getWidgetEvent(StyledText.class,
				"extendedModifyText").getListenerInterfaceClass());
		assertEquals(VisibilityWindowListener.class, eventRegistry.getWidgetEvent(Browser.class,
				"visibilityWindowHide").getListenerInterfaceClass());
	}

}
