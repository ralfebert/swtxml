package com.swtxml.util.parser;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

public class Splitter {

	private final char preferredSeparator;
	private final String separators;

	public Splitter(String separators) {
		this.preferredSeparator = separators.charAt(0);
		this.separators = separators;
	}

	public String join(Object[] values) {
		return StringUtils.join(values, preferredSeparator);
	}

	public String join(Collection<?> values) {
		return this.join(values.toArray());

	}

	public String[] split(String value) {
		return StringUtils.split(value, separators);
	}

	public char getPreferredSeparator() {
		return preferredSeparator;
	}

	public String getSeparators() {
		return separators;
	}

	public boolean isSeparator(char pchar) {
		return (separators.indexOf(pchar) >= 0);
	}
}