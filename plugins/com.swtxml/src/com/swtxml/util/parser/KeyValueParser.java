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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class KeyValueParser {

	public final static Splitter VALUE_SPLITTER = new Splitter(";");
	public final static Splitter KEY_VALUE_SPLITTER = new Splitter(":");

	public static Map<String, String> parse(String value) {
		return parse(value, Strictness.STRICT);
	}

	public static Map<String, String> parse(String value, Strictness strictness) {
		Map<String, String> values = new HashMap<String, String>();
		for (String valuePair : VALUE_SPLITTER.split(value)) {
			if (StringUtils.isNotBlank(valuePair)) {
				String[] keyValue = KEY_VALUE_SPLITTER.split(valuePair);
				if (keyValue.length != 2) {
					if (strictness == Strictness.STRICT) {
						throw new ParseException("Invalid format: \"" + valuePair + "\" ");
					} else {
						if (keyValue.length == 1) {
							keyValue = new String[] { keyValue[0], "" };
						}
					}
				}
				values.put(keyValue[0].trim(), keyValue[1].trim());
			}
		}
		return values;
	}

}
