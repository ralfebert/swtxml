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
import com.swtxml.events.internal.SwtEvents;
import com.swtxml.events.registry.WidgetEventListenerMethod;
import com.swtxml.tinydom.ITagVisitor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

/**
 * CreateEventListeners processes on:eventName="responderMethod" attribute
 * values by adding a listener to the widget that delegates the event to a
 * method "responder"."responderMethod"(). It only applies to attributes in the
 * given namespace and a given event registry.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class AddEventListeners implements ITagVisitor {

	private Object responder;
	private SwtEvents namespace;

	public AddEventListeners(Object responder, SwtEvents namespace) {
		this.responder = responder;
		this.namespace = namespace;
	}

	public void visit(Tag tag) {
		Collection<IAttributeDefinition> events = tag.getAttributes(namespace);
		if (!events.isEmpty()) {

			Widget widget = tag.getAdapter(Widget.class);
			Assert.isNotNull(widget, String.format(
					"Tag %s has event attributes %s, but is not a Widget!", tag.getName(), events));

			for (IAttributeDefinition eventAttribute : events) {
				String responderMethodName = tag.getAttributeValue(namespace, eventAttribute);
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

		WidgetEventListenerMethod event = namespace.getEvents().getWidgetEvent(widget.getClass(),
				eventName);
		Assert.isNotNull(event, String.format("No event %s in %s found!", eventName, widget
				.getClass()));

		Method responderMethod = Reflector.findMethods(Visibility.PRIVATE, Subclasses.INCLUDE)
				.name(responderMethodName.trim()).optionalParameter(event.getEventClass())
				.exactOne(responder.getClass());

		InvocationHandler handler = new ListenerDelegatingInvocationHandler(eventName, responder,
				responderMethod);

		Object listener = Proxy.newProxyInstance(responder.getClass().getClassLoader(),
				new Class[] { event.getListenerInterfaceClass() }, handler);

		event.addListenerToWidget(widget, listener);
	}

}
