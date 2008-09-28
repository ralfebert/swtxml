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
package com.swtxml.swt.types;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.swt.SwtInfo;
import com.swtxml.util.context.Context;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.PropertiesContentAssist;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class LayoutDataType implements IType<Object>, IContentAssistable {

	public Object convert(String value) {
		Layout layout = Context.adaptTo(Layout.class);
		if (layout == null) {
			throw new ParseException("LayoutData can only be used in a Layout context");
		}
		return createLayoutData(layout, value);
	}

	public Object createLayoutData(Layout parentLayout, String value) {
		Map<String, String> layoutConstraints = KeyValueParser.parse(value);

		Class<?> layoutDataClass = getLayoutDataClass(parentLayout);
		if (layoutDataClass == null) {
			throw new ParseException("Layout " + parentLayout.getClass().getSimpleName()
					+ " doesn't allow layoutData!");
		}

		Object layoutData;
		try {
			layoutData = layoutDataClass.newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
		SwtInfo.LAYOUT_PROPERTIES.getProperties(layoutData.getClass()).getInjector(layoutData)
				.setPropertyValues(layoutConstraints);
		return layoutData;
	}

	private Class<?> getLayoutDataClass(Layout layout) {
		if (layout == null) {
			return null;
		}
		if (layout instanceof RowLayout) {
			return RowData.class;
		}
		if (layout instanceof GridLayout) {
			return GridData.class;
		}
		if (layout instanceof FormLayout) {
			return FormData.class;
		}
		return null;
	}

	public List<Match> getProposals(Match match) {
		final Layout layout = Context.adaptTo(Layout.class);
		if (layout == null) {
			return Collections.emptyList();
		}
		// TODO: overriding the class is not necessary here because the
		// decision is not dependent on the values
		PropertiesContentAssist assist = new PropertiesContentAssist() {
			@Override
			protected ClassProperties<?> getClassProperties(Map<String, String> values) {
				Class<?> layoutData = getLayoutDataClass(layout);
				if (layoutData == null) {
					return null;
				}
				return SwtInfo.LAYOUT_PROPERTIES.getProperties(layoutData);
			}
		};
		return assist.getProposals(match);
	}
}
