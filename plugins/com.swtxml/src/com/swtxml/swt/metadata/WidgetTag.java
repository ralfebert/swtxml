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
package com.swtxml.swt.metadata;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.impl.AttributeDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.types.StyleType;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.Property;
import com.swtxml.util.types.SimpleTypes;

public class WidgetTag implements ITagDefinition {

	private String className;
	private Class<? extends Widget> widgetClass;

	private Map<String, IAttributeDefinition> attributes;

	public WidgetTag(Class<? extends Widget> widgetClass) {
		this.widgetClass = widgetClass;

		ClassProperties<? extends Widget> properties = SwtInfo.WIDGET_PROPERTIES
				.getProperties(widgetClass);
		attributes = new HashMap<String, IAttributeDefinition>();
		for (Property property : properties.getProperties().values()) {
			WidgetAttribute attribute = new WidgetAttribute(property);
			attributes.put(attribute.getName(), attribute);
		}
		attributes.put("id", new AttributeDefinition("id", SimpleTypes.STRING));
		Collection<String> allowedWidgetStyles = SwtInfo.WIDGETS.getAllowedStylesFor(widgetClass);
		attributes.put("style", new AttributeDefinition("style", new StyleType(SwtInfo.SWT
				.filter(allowedWidgetStyles))));
	}

	public Class<? extends Widget> getWidgetClass() {
		return widgetClass;
	}

	public String getName() {
		return widgetClass.getSimpleName();
	}

	@Override
	public String toString() {
		return "SwtTag[" + className + "]";
	}

	public int compareTo(ITagDefinition o) {
		return this.getName().compareTo(o.getName());
	}

	public IAttributeDefinition getAttribute(String name) {
		return attributes.get(name);
	}

	public Set<String> getAttributeNames() {
		return Collections.unmodifiableSet(attributes.keySet());
	}

	public boolean isAllowedIn(ITagDefinition parentTagDefinition) {
		if (parentTagDefinition == ITagDefinition.ROOT) {
			return Composite.class.isAssignableFrom(getWidgetClass());
		}
		if (parentTagDefinition instanceof WidgetTag) {
			Class<? extends Widget> actualParentClass = ((WidgetTag) parentTagDefinition)
					.getWidgetClass();
			Class<?> allowedParentClass = SwtInfo.WIDGETS.getAllowedParentType(getWidgetClass());
			if (TabItem.class.equals(actualParentClass)) {
				return Control.class.isAssignableFrom(getWidgetClass());
			}
			if (TabFolder.class.equals(actualParentClass)) {
				return TabItem.class.isAssignableFrom(getWidgetClass());
			}
			return allowedParentClass.isAssignableFrom(actualParentClass);
		}
		return false;
	}
}
