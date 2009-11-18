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
package com.swtxml.swt.visitor;

import java.lang.reflect.Constructor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.metadata.WidgetTag;
import com.swtxml.tinydom.ITagVisitor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.reflector.ReflectorException;

public class BuildWidgets implements ITagVisitor {

	private Composite parent;

	public BuildWidgets(Composite parent) {
		this.parent = parent;
	}

	public void visit(Tag tag) {
		if (!(tag.getTagDefinition() instanceof WidgetTag)) {
			return;
		}

		if (tag.isRoot()) {
			if (!tag.getName().equals(Composite.class.getSimpleName())) {
				throw new ParseException("Invalid root tag " + tag.getName() + ", expected <"
						+ Composite.class.getSimpleName() + ">");
			}
			tag.addAdapter(parent);
			return;
		}

		WidgetTag widgetTag = (WidgetTag) tag.getTagDefinition();

		Constructor<?> constructor = SwtInfo.WIDGETS.getWidgetConstructor(widgetTag
				.getWidgetClass());
		Class<?> parentClass = constructor.getParameterTypes()[0];

		Composite parent = (Composite) tag.getAdapterParentRecursive(parentClass);
		// TODO: exception "allowed are" should show only allowed styles, not
		// all from SWT
		Integer style = SwtInfo.SWT.getIntValue(tag.getAttributeValue("style"));

		Widget widget = build(constructor, parent, style == null ? SWT.NONE : style);
		tag.addAdapter(widget);
	}

	public Widget build(Constructor<?> constructor, Object parent, int style) {
		try {
			return (Widget) constructor.newInstance(new Object[] { parent, style });
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

}
