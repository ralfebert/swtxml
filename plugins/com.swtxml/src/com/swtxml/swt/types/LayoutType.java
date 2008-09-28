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

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.swt.SwtInfo;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.parser.Strictness;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.IInjector;
import com.swtxml.util.properties.PropertiesContentAssist;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class LayoutType implements IType<Layout>, IContentAssistable {

	private static final String LAYOUT_KEY = "layout";
	// TODO: use classes.txt file (same as for widgets)
	private final String[] LAYOUTS = new String[] { "fill", "row", "grid", "form" };
	private final static String SWT_LAYOUT_PACKAGE = RowLayout.class.getPackage().getName();

	public Layout convert(String value) {
		return convert(value, Strictness.STRICT);
	}

	public Layout convert(String value, Strictness strictness) {
		Map<String, String> layoutConstraints = KeyValueParser.parse(value, strictness);

		String layoutName = layoutConstraints.remove(LAYOUT_KEY);
		if (layoutName == null) {
			if (strictness == Strictness.STRICT) {
				throw new ParseException("no layout specified");
			} else {
				return null;
			}
		}

		Layout layout = createLayout(layoutName, Strictness.STRICT);

		IInjector injector = SwtInfo.LAYOUT_PROPERTIES.getProperties(layout.getClass())
				.getInjector(layout);
		injector.setPropertyValues(layoutConstraints);

		return layout;
	}

	private Layout createLayout(String layoutName, Strictness strictness) {
		try {
			return getLayoutClass(layoutName, strictness).newInstance();
		} catch (Exception e) {
			if (strictness == Strictness.STRICT) {
				throw new ReflectorException(e);
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Layout> getLayoutClass(String layoutName, Strictness strictness) {
		String className = SWT_LAYOUT_PACKAGE + "." + StringUtils.capitalize(layoutName) + "Layout";
		try {
			return (Class<? extends Layout>) Class.forName(className);
		} catch (Exception e) {
			if (strictness == Strictness.STRICT) {
				throw new ReflectorException(e);
			}
			return null;
		}
	}

	public List<Match> getProposals(Match match) {
		// TODO: this is a bit messy at the moment, because only the class
		// property definition is used for completion, so we have to smuggle the
		// "special" layout selector property in
		// maybe: separate namespace layout:[grid]=""
		PropertiesContentAssist assist = new PropertiesContentAssist() {

			@Override
			protected ClassProperties<?> getClassProperties(Map<String, String> values) {
				String layoutName = values.get("layout");
				if (layoutName == null) {
					return null;
				}
				Class<? extends Layout> layoutClass = getLayoutClass(layoutName, Strictness.NICE);
				if (layoutClass == null) {
					return null;
				}
				return SwtInfo.LAYOUT_PROPERTIES.getProperties(layoutClass);
			}

			@Override
			protected List<Match> keyProposals(Map<String, String> values, Match match) {
				if (getClassProperties(values) == null) {
					return match.propose(LAYOUT_KEY + ":");
				}
				return super.keyProposals(values, match);
			}

			@Override
			protected List<Match> valueProposals(Map<String, String> values, String key, Match match) {
				if ("layout".equals(key.toLowerCase())) {
					return match.propose(LAYOUTS);
				}
				return super.valueProposals(values, key, match);
			}

		};
		return assist.getProposals(match);
	}
}
