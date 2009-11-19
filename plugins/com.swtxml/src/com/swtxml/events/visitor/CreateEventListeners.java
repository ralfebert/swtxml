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
package com.swtxml.events.visitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.events.impl.Events;
import com.swtxml.events.registry.WidgetEvent;
import com.swtxml.tinydom.ITagVisitor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

/**
 * CreateEventListeners processes on:eventName="responderMethod" attribute
 * values by adding a listener to the widget that delegates the event to a
 * method "responder"."responderMethod"().
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class CreateEventListeners implements ITagVisitor {

	private Object responder;

	public CreateEventListeners(Object responder) {
		this.responder = responder;
	}

	public void visit(Tag tag) {
		Collection<IAttributeDefinition> events = tag.getAttributes(Events.NAMESPACE);
		if (!events.isEmpty()) {

			Widget widget = tag.getAdapter(Widget.class);
			Assert.isNotNull(widget, String.format(
					"Tag %s has event attributes %s, but is not a Widget!", tag.getName(), events));

			for (IAttributeDefinition eventAttribute : events) {
				String responderMethodName = tag
						.getAttributeValue(Events.NAMESPACE, eventAttribute);
				setupListener(widget, eventAttribute.getName(), responderMethodName);
			}

		}
	}

	/**
	 * setupListener resolves the event handling method named
	 * responderMethodName in the responder class. It then creates a SWT
	 * listener proxy object that delegates to this responder method whenever
	 * listener.<eventName> is called. This listener is added to the widget.
	 */
	void setupListener(Widget widget, final String eventName, String responderMethodName) {

		WidgetEvent event = Events.EVENTS.getWidgetEvent(widget.getClass(), eventName);
		Assert.isNotNull(event, String.format("No event %s in %s found!", eventName, widget
				.getClass()));

		Method responderMethod = Reflector.findMethods(Visibility.PRIVATE, Subclasses.INCLUDE)
				.name(responderMethodName.trim()).optionalParameter(event.getEventParamType())
				.exactOne(responder.getClass());

		InvocationHandler handler = new ListenerDelegatingInvocationHandler(eventName, responder,
				responderMethod);

		Class<?> listenerType = event.getListenerType();
		Object listener = Proxy.newProxyInstance(responder.getClass().getClassLoader(),
				new Class[] { listenerType }, handler);

		event.addListenerToWidget(widget, listener);
	}

}
