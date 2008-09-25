package com.swtxml.converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;

import com.swtxml.converter.SimpleTypeConverters.BooleanConverter;
import com.swtxml.converter.SimpleTypeConverters.CharacterConverter;
import com.swtxml.converter.SimpleTypeConverters.FloatConverter;
import com.swtxml.converter.SimpleTypeConverters.IntegerConverter;

public class SwtConverters {

	private static final Map<Class<?>, IConverter<?>> CONVERTERS = new HashMap<Class<?>, IConverter<?>>();
	private static final Map<PropertyNameAndClass, IConverter<?>> PROPERTYNAME_CONVERTERS = new HashMap<PropertyNameAndClass, IConverter<?>>();

	static {
		CONVERTERS.put(Color.class, new ColorConverter());
		BooleanConverter booleanConverter = new BooleanConverter();
		CONVERTERS.put(Boolean.class, booleanConverter);
		CONVERTERS.put(Boolean.TYPE, booleanConverter);
		IntegerConverter integerConverter = new IntegerConverter();
		CONVERTERS.put(Integer.class, integerConverter);
		CONVERTERS.put(Integer.TYPE, integerConverter);
		FloatConverter floatConverter = new FloatConverter();
		CONVERTERS.put(Float.class, floatConverter);
		CONVERTERS.put(Float.TYPE, floatConverter);
		CharacterConverter characterConverter = new CharacterConverter();
		CONVERTERS.put(Character.class, characterConverter);
		CONVERTERS.put(Character.TYPE, characterConverter);

		PROPERTYNAME_CONVERTERS.put(new PropertyNameAndClass("style", Integer.TYPE),
				new StyleConverter());
	}

	@SuppressWarnings("unchecked")
	public static <T> IConverter<T> to(Class<T> clazz) {
		return (IConverter<T>) CONVERTERS.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public static <T> IConverter<T> forProperty(String name, Class<T> clazz) {
		return (IConverter<T>) PROPERTYNAME_CONVERTERS.get(new PropertyNameAndClass(name, clazz));
	}

}
