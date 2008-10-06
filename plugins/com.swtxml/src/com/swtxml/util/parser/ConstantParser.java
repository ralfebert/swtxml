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
package com.swtxml.util.parser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

public class ConstantParser {

	private Map<String, Integer> constantMap = new HashMap<String, Integer>();

	public static final Splitter SPLITTER = new Splitter(",;|");

	public ConstantParser(Class<?>... constantClasses) {
		for (Class<?> cl : constantClasses) {
			if (!constantMap.containsKey(cl)) {
				constantMap.putAll(extractConstants(cl));
			}
		}
	}

	private ConstantParser(Map<String, Integer> constantMap) {
		this.constantMap = constantMap;
	}

	private Map<String, Integer> extractConstants(Class<?> cl) {
		Map<String, Integer> constants = new HashMap<String, Integer>();
		try {
			Collection<Field> fields = Reflector.findFields(Visibility.PUBLIC, Subclasses.NONE)
					.isStatic(true).type(Integer.TYPE).all(cl);
			for (Field field : fields) {
				constants.put(field.getName(), field.getInt(cl));
			}
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
		return constants;
	}

	public int getIntValue(String value) {
		int style = 0;
		if (value == null) {
			return style;
		}
		String[] values = SPLITTER.split(value);
		for (String v : values) {
			v = v.trim().toUpperCase();

			Integer constant = null;
			constant = constantMap.get(v);

			if (constant == null) {
				List<String> constants = new ArrayList<String>(constantMap.keySet());
				Collections.sort(constants);
				throw new ParseException("Unknown style constant: " + v + ", allowed are: "
						+ SPLITTER.join(constants));
			}

			style |= constant;
		}
		return style;

	}

	public ConstantParser filter(String value) {
		return filter(Arrays.asList(SPLITTER.split(value)));
	}

	public ConstantParser filter(Iterable<String> values) {
		Map<String, Integer> matches = new HashMap<String, Integer>();
		for (String v : values) {
			v = v.trim().toUpperCase();
			Integer constant = null;
			constant = constantMap.get(v);
			if (constant == null) {
				List<String> constants = new ArrayList<String>(constantMap.keySet());
				Collections.sort(constants);
				throw new ParseException("Unknown style constant: " + v + ", allowed are: "
						+ StringUtils.join(constants, ", "));
			}

			matches.put(v, constant);
		}
		return new ConstantParser(matches);

	}

	public Collection<String> getConstants() {
		return constantMap.keySet();
	}
}