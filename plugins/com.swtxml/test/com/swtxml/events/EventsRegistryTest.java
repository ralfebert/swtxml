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
package com.swtxml.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.junit.Test;

public class EventsRegistryTest {

	@Test
	public void getAllEventNames() {
		assertTrue(Events.EVENTS.getAllEventNames().contains("widgetSelected"));
		assertTrue(Events.EVENTS.getAllEventNames().contains("focusGained"));
	}

	@Test
	public void isEventAvailableFor() {
		assertTrue(Events.EVENTS.isEventAvailableFor("widgetSelected", Button.class));
		assertFalse(Events.EVENTS.isEventAvailableFor("widgetSelected", Control.class));
		assertTrue(Events.EVENTS.isEventAvailableFor("focusGained", Button.class));
		assertTrue(Events.EVENTS.isEventAvailableFor("focusGained", Control.class));
	}

}
