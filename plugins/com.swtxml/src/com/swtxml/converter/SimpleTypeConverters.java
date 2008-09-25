package com.swtxml.converter;

public class SimpleTypeConverters {

	public static class BooleanConverter implements IConverter<Boolean> {

		public Boolean convert(String value) {
			return Boolean.parseBoolean(value);
		}

	}

	public static class IntegerConverter implements IConverter<Integer> {

		public Integer convert(String value) {
			return Integer.parseInt(value);
		}
	}

}
