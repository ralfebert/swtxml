package com.swtxml.converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;

import com.swtxml.converter.SimpleTypeConverters.BooleanConverter;
import com.swtxml.converter.SimpleTypeConverters.IntegerConverter;

public class SwtConverters {

	private static final Map<Class<?>, IConverter<?>> CONVERTERS = new HashMap<Class<?>, IConverter<?>>();

	static {
		CONVERTERS.put(Color.class, new ColorConverter());
		BooleanConverter booleanConverter = new BooleanConverter();
		CONVERTERS.put(Boolean.class, booleanConverter);
		CONVERTERS.put(Boolean.TYPE, booleanConverter);
		IntegerConverter integerConverter = new IntegerConverter();
		CONVERTERS.put(Integer.class, integerConverter);
		CONVERTERS.put(Integer.TYPE, integerConverter);
	}

	@SuppressWarnings("unchecked")
	public static <T> IConverter<T> to(Class<T> clazz) {
		return (IConverter<T>) CONVERTERS.get(clazz);
	}

}
