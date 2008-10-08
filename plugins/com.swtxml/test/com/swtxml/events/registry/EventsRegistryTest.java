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
import org.junit.Test;

import com.swtxml.events.Events;

public class EventsRegistryTest {

	@Test
	public void getAllEventNames() {
		assertTrue(Events.EVENTS.getAllEventNames().contains("widgetSelected"));
		assertTrue(Events.EVENTS.getAllEventNames().contains("focusGained"));
	}

	@Test
	public void isEventAvailableFor() {
		assertNotNull(Events.EVENTS.getWidgetEvent(Button.class, "widgetSelected"));
		assertNull(Events.EVENTS.getWidgetEvent(Control.class, "widgetSelected"));
		assertNotNull(Events.EVENTS.getWidgetEvent(Button.class, "focusGained"));
		assertNotNull(Events.EVENTS.getWidgetEvent(Control.class, "focusGained"));
	}

	@Test
	public void testGetEventInterface() {
		assertEquals(SelectionListener.class, Events.EVENTS.getWidgetEvent(Button.class,
				"widgetSelected").getListenerType());
		assertEquals(FocusListener.class, Events.EVENTS.getWidgetEvent(Button.class, "focusGained")
				.getListenerType());
		assertEquals(ModifyListener.class, Events.EVENTS.getWidgetEvent(Text.class, "modifyText")
				.getListenerType());
		assertEquals(ModifyListener.class, Events.EVENTS.getWidgetEvent(StyledText.class,
				"modifyText").getListenerType());
		assertEquals(ExtendedModifyListener.class, Events.EVENTS.getWidgetEvent(StyledText.class,
				"extendedModifyText").getListenerType());
		assertEquals(VisibilityWindowListener.class, Events.EVENTS.getWidgetEvent(Browser.class,
				"visibilityWindowHide").getListenerType());
	}

}
