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
import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

public class EventsRegistry {

	private Set<String> allEventNames = new HashSet<String>();
	private Map<Class<?>, Set<String>> widgetClassToEventNamesList = new HashMap<Class<?>, Set<String>>();
	private Map<String, Class<?>> eventNameToListenerTypeMap = new HashMap<String, Class<?>>();
	private Map<String, Class<?>> eventNameToEventParamTypeMap = new HashMap<String, Class<?>>();

	public EventsRegistry() {
		for (String widgetClassName : SwtInfo.WIDGETS.getWidgetClassNames()) {
			Class<? extends Widget> widgetClass = SwtInfo.WIDGETS.getWidgetClass(widgetClassName);
			Set<String> eventNames = new HashSet<String>();

			Collection<Method> listenerMethods = Reflector.findMethods(Visibility.PUBLIC,
					Subclasses.INCLUDE).parameters(EventListener.class)
					.nameMatches("add.+Listener").all(widgetClass);

			for (Method listenerMethod : listenerMethods) {
				Class<?> listenerType = listenerMethod.getParameterTypes()[0];
				for (Method eventMethod : listenerType.getMethods()) {
					String eventName = eventMethod.getName();
					eventNames.add(eventName);
					eventNameToListenerTypeMap.put(eventName, listenerType);
					eventNameToEventParamTypeMap.put(eventName, eventMethod.getParameterTypes()[0]);
				}
			}

			allEventNames.addAll(eventNames);
			widgetClassToEventNamesList.put(widgetClass, eventNames);
		}

	}

	public Collection<String> getAllEventNames() {
		return Collections.unmodifiableSet(allEventNames);
	}

	public boolean isEventAvailableFor(String eventName, Class<?> widgetClass) {
		Set<String> events = widgetClassToEventNamesList.get(widgetClass);
		return events != null && events.contains(eventName);
	}

	public Class<?> getEventInterface(String eventName) {
		return eventNameToListenerTypeMap.get(eventName);
	}

	public Class<?> getEventParamType(String eventName) {
		return eventNameToEventParamTypeMap.get(eventName);
	}

}
