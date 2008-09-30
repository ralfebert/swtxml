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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.SwtInfo;

public class EventsRegistry {

	private Set<String> allEventNames = new HashSet<String>();
	private Map<Class<?>, Set<String>> widgetClassToEventNameList = new HashMap<Class<?>, Set<String>>();

	public EventsRegistry() {
		for (String widgetClassName : SwtInfo.WIDGETS.getWidgetClassNames()) {
			Class<? extends Widget> widgetClass = SwtInfo.WIDGETS.getWidgetClass(widgetClassName);
			Set<String> eventNames = new HashSet<String>();
			for (Method listenerMethod : widgetClass.getMethods()) {
				String name = listenerMethod.getName();
				if (listenerMethod.getParameterTypes().length == 1
						&& EventListener.class
								.isAssignableFrom(listenerMethod.getParameterTypes()[0])
						&& name.startsWith("add") && name.endsWith("Listener")
						&& !name.equals("addListener")) {
					Class<?> listenerType = listenerMethod.getParameterTypes()[0];

					for (Method eventMethod : listenerType.getMethods()) {
						eventNames.add(eventMethod.getName());
					}
				}
			}
			allEventNames.addAll(eventNames);
			widgetClassToEventNameList.put(widgetClass, eventNames);
		}

	}

	public Collection<String> getAllEventNames() {
		return Collections.unmodifiableSet(allEventNames);
	}

	public boolean isEventAvailableFor(String eventName, Class<?> widgetClass) {
		Set<String> events = widgetClassToEventNameList.get(widgetClass);
		return events != null && events.contains(eventName);
	}
}
