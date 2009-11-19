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

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

class WidgetEvents {

	private Map<String, WidgetEvent> events = new HashMap<String, WidgetEvent>();

	public WidgetEvents(Class<? extends Widget> widgetClass) {

		Collection<Method> listenerMethods = Reflector.findMethods(Visibility.PUBLIC,
				Subclasses.INCLUDE).parameters(EventListener.class).nameMatches("add.+Listener")
				.all(widgetClass);

		for (Method listenerAddMethod : listenerMethods) {
			Class<?> listenerType = listenerAddMethod.getParameterTypes()[0];
			for (Method listenerMethod : listenerType.getMethods()) {
				String eventName = getEventName(listenerAddMethod, listenerMethod, listenerType);
				Class<?> eventParamType = listenerMethod.getParameterTypes()[0];
				WidgetEvent widgetEvent = new WidgetEvent(listenerType, eventParamType);
				WidgetEvent oldValue = events.put(eventName, widgetEvent);
				if (oldValue != null) {
					String msg = String.format(
							"Event %s from %s causes conflicting event attributes for widget %s!",
							eventName, listenerType, widgetClass.getName());
					throw new IllegalStateException(msg);
				}
			}
		}
	}

	private String getEventName(Method listenerMethod, Method eventMethod, Class<?> listenerType) {
		if (StyledText.class == listenerMethod.getDeclaringClass()
				&& ExtendedModifyListener.class == listenerType) {
			return "extendedModifyText";
		}
		if (Browser.class == listenerMethod.getDeclaringClass()) {
			return StringUtils.uncapitalize(StringUtils.replace(listenerType.getSimpleName(),
					"Listener", ""))
					+ StringUtils.capitalize(eventMethod.getName());
		}
		return eventMethod.getName();
	}

	public Collection<String> getEventNames() {
		return events.keySet();
	}

	public WidgetEvent getEvent(String eventName) {
		return events.get(eventName);
	}

}
