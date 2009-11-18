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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

public class WidgetEvent {

	private final Class<?> listenerType;
	private final Class<?> eventParamType;

	public WidgetEvent(Class<?> listenerType, Class<?> eventParamType) {
		super();
		this.listenerType = listenerType;
		this.eventParamType = eventParamType;
	}

	public Class<?> getListenerType() {
		return listenerType;
	}

	public Class<?> getEventParamType() {
		return eventParamType;
	}

	public void addListenerToWidget(Widget widget, Object listener) {
		Method addMethod = Reflector.findMethods(Visibility.PUBLIC, Subclasses.INCLUDE)
				.nameStartsWith("add").parameters(listenerType).exactOne(widget.getClass());
		try {
			addMethod.invoke(widget, listener);
		} catch (Exception e) {
			new ReflectorException(e);
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(listenerType).append(eventParamType).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[eventParamType=" + eventParamType + ", listenerType="
				+ listenerType + "]";
	}
}