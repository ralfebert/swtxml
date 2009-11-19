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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.SwtInfo;

public class EventsRegistry {

	private final Set<String> allEventNames;
	private final Map<Class<?>, WidgetEvents> widgetClasses;

	public EventsRegistry() {
		Set<String> bAllEventNames = new HashSet<String>();
		Map<Class<?>, WidgetEvents> bWidgetClasses = new HashMap<Class<?>, WidgetEvents>();
		for (String widgetClassName : SwtInfo.WIDGETS.getWidgetClassNames()) {
			Class<? extends Widget> widgetClass = SwtInfo.WIDGETS.getWidgetClass(widgetClassName);
			WidgetEvents events = new WidgetEvents(widgetClass);
			bWidgetClasses.put(widgetClass, events);
			bAllEventNames.addAll(events.getEventNames());
		}
		widgetClasses = Collections.unmodifiableMap(bWidgetClasses);
		allEventNames = Collections.unmodifiableSet(bAllEventNames);
	}

	public Collection<String> getAllEventNames() {
		return allEventNames;
	}

	public WidgetEventListenerMethod getWidgetEvent(Class<?> widgetClass, String eventName) {
		WidgetEvents widgetEvents = widgetClasses.get(widgetClass);
		return (widgetEvents != null) ? widgetEvents.getEvent(eventName) : null;
	}

}