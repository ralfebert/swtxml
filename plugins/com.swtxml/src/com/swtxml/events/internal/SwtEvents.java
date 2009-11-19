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
package com.swtxml.events.internal;

import com.swtxml.definition.internal.NamespaceDefinition;
import com.swtxml.events.registry.EventsRegistry;
import com.swtxml.swt.SwtInfo;

public class SwtEvents extends NamespaceDefinition {

	private static final SwtEvents namespace = new SwtEvents();

	public static SwtEvents getNamespace() {
		return namespace;
	}

	private final EventsRegistry events = EventsRegistry.scanWidgets(SwtInfo.WIDGETS);

	private SwtEvents() {
		super("http://www.swtxml.com/events");
		for (String eventName : events.getAllEventNames()) {
			this.defineForeignAttribute(new EventForeignAttribute(eventName));
		}
	}

	@Deprecated
	public EventsRegistry getEvents() {
		return events;
	}

}
