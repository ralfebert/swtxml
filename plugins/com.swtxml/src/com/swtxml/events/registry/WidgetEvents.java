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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

class WidgetEvents {

	private Map<String, WidgetEventListenerMethod> events = new HashMap<String, WidgetEventListenerMethod>();

	WidgetEvents(Class<? extends Widget> widgetClass) {

		Collection<Method> listenerMethods = Reflector.findMethods(Visibility.PUBLIC,
				Subclasses.INCLUDE).parameters(EventListener.class).nameMatches("add.+Listener")
				.all(widgetClass);

		for (Method listenerAddMethod : listenerMethods) {
			Class<?> listenerType = listenerAddMethod.getParameterTypes()[0];
			for (Method listenerMethod : listenerType.getMethods()) {
				WidgetEventListenerMethod event = new WidgetEventListenerMethod(listenerAddMethod,
						listenerMethod);
				WidgetEventListenerMethod oldValue = events.put(event.getName(), event);
				if (oldValue != null) {
					String msg = String.format(
							"Event %s from %s causes conflicting event attributes for widget %s!",
							event.getName(), listenerType, widgetClass.getName());
					throw new IllegalStateException(msg);
				}
			}
		}
	}

	Collection<String> getEventNames() {
		return events.keySet();
	}

	WidgetEventListenerMethod getEvent(String eventName) {
		return events.get(eventName);
	}

}
