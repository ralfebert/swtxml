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
import com.swtxml.definition.ITagScope;
import com.swtxml.definition.internal.AttributeDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.types.StyleType;
import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.Property;
import com.swtxml.util.types.SimpleTypes;

public class WidgetTag implements ITagDefinition, ITagScope {

	private Class<? extends Widget> widgetClass;

	private Map<String, IAttributeDefinition> attributes;

	public WidgetTag(Class<? extends Widget> widgetClass) {
		setWidgetClass(widgetClass);
		addStyles(widgetClass);
	}

	public WidgetTag(Class<? extends Widget> widgetClass, String allowedStyles) {
		setWidgetClass(widgetClass);
		addStyles(SwtInfo.SWT.filter(allowedStyles));
	}

	private void setWidgetClass(Class<? extends Widget> widgetClass) {
		this.widgetClass = widgetClass;

		ClassProperties<? extends Widget> properties = SwtInfo.WIDGET_PROPERTIES
				.getProperties(widgetClass);
		attributes = new HashMap<String, IAttributeDefinition>();
		for (Property property : properties.getProperties().values()) {
			PropertyAttribute attribute = new PropertyAttribute(property);
			attributes.put(attribute.getName(), attribute);
		}

		attributes.put("id", new AttributeDefinition("id", SimpleTypes.STRING));
	}

	private void addStyles(Class<? extends Widget> widgetClass) {
		Collection<String> allowedWidgetStyles = SwtInfo.WIDGETS.getAllowedStylesFor(widgetClass);
		addStyles(SwtInfo.SWT.filter(allowedWidgetStyles));
	}

	private void addStyles(ConstantParser styles) {
		if (!styles.getConstants().isEmpty()) {
			attributes.put("style", new AttributeDefinition("style", new StyleType(styles)));
		}
	}

	public Class<? extends Widget> getWidgetClass() {
		return widgetClass;
	}

	public String getName() {
		return widgetClass.getSimpleName();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + widgetClass + "]";
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
