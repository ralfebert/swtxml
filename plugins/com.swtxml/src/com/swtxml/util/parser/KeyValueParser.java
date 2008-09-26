package com.swtxml.util.parser;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class KeyValueParser {

	public static Map<String, String> parse(String value) {
		Map<String, String> values = new HashMap<String, String>();
		for (String valuePair : value.split(";")) {
			if (StringUtils.isNotBlank(valuePair)) {
				String[] keyValue = valuePair.split(":");
				if (keyValue.length != 2) {
					throw new ParseException("Invalid format: \"" + valuePair + "\" ");
				}
				values.put(keyValue[0].trim(), keyValue[1].trim());
			}
		}
		return values;
	}

}
