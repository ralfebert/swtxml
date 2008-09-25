package com.swtxml.converter;

import static com.swtxml.converter.PropertyMatcher.ALL_CLASSES;
import static com.swtxml.converter.PropertyMatcher.ALL_PROPERTIES;

import com.swtxml.util.FormatException;

public class SimpleTypeConverters {

	public static void addSimpleTypes(InjectorDefinition inj) {
		inj.addConverter(new BooleanConverter(), ALL_CLASSES, ALL_PROPERTIES, Boolean.class,
				Boolean.TYPE);
		inj.addConverter(new IntegerConverter(), ALL_CLASSES, ALL_PROPERTIES, Integer.class,
				Integer.TYPE);
		inj.addConverter(new FloatConverter(), ALL_CLASSES, ALL_PROPERTIES, Float.class, Float.TYPE);
		inj.addConverter(new CharacterConverter(), ALL_CLASSES, ALL_PROPERTIES, Character.class,
				Character.TYPE);
		inj.addConverter(new StringConverter(), ALL_CLASSES, ALL_PROPERTIES, String.class);
	}

	public static class StringConverter implements IConverter<String> {
		public String convert(String value) {
			// TODO: i18n
			if (value.startsWith("%")) {
				throw new FormatException("i18n for string properties is currently unsupported");
			}
			return value;
		}
	}

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

	public static class FloatConverter implements IConverter<Float> {
		public Float convert(String value) {
			return Float.parseFloat(value);
		}
	}

	public static class CharacterConverter implements IConverter<Character> {
		public Character convert(String value) {
			return value.charAt(0);
		}
	}

}
