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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.util.reflector.ReflectorException;

public class WidgetEventListenerMethod {

	private final Method listenerAddMethod;
	private final Method listenerMethod;

	public WidgetEventListenerMethod(Method listenerAddMethod, Method listenerMethod) {
		super();
		this.listenerAddMethod = listenerAddMethod;
		this.listenerMethod = listenerMethod;
	}

	public Class<?> getListenerInterfaceClass() {
		return listenerAddMethod.getParameterTypes()[0];
	}

	public Class<?> getEventClass() {
		return listenerMethod.getParameterTypes()[0];
	}

	public void addListenerToWidget(Widget widget, Object listener) {
		try {
			listenerAddMethod.invoke(widget, listener);
		} catch (Exception e) {
			new ReflectorException(e);
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getListenerInterfaceClass()).append(getEventClass())
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public String toString() {
		return String.format("%s[%s, %s]", getClass().getSimpleName(), listenerAddMethod.getName(),
				listenerMethod.getName());
	}

	public String getName() {
		if (StyledText.class == listenerAddMethod.getDeclaringClass()
				&& ExtendedModifyListener.class == getListenerInterfaceClass()) {
			return "extendedModifyText";
		}
		if (Browser.class == listenerAddMethod.getDeclaringClass()) {
			return StringUtils.uncapitalize(StringUtils.replace(getListenerInterfaceClass()
					.getSimpleName(), "Listener", ""))
					+ StringUtils.capitalize(listenerMethod.getName());
		}
		return listenerMethod.getName();
	}
}