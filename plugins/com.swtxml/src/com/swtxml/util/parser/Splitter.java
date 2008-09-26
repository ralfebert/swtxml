package com.swtxml.util.parser;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

public class Splitter {

	private char preferredSeparator;
	private String allowedSeparators;

	private Splitter(char preferredSeparator, String allowedSeparators) {
		this.preferredSeparator = preferredSeparator;
		this.allowedSeparators = allowedSeparators;
	}

	public static Splitter none() {
		return new Splitter(' ', null);
	}

	public static Splitter allowMultiple(char preferredSeparatar, String allowedSeparators) {
		return new Splitter(preferredSeparatar, allowedSeparators);
	}

	public String join(Object[] values) {
		if (allowedSeparators == null) {
			return StringUtils.join(values, "");
		} else {
			return StringUtils.join(values, preferredSeparator);
		}
	}

	public String join(Collection<?> values) {
		return this.join(values.toArray());

	}

	public String[] split(String value) {
		if (allowedSeparators == null) {
			return new String[] { value };
		} else {
			return StringUtils.split(value, allowedSeparators);
		}
	}

}