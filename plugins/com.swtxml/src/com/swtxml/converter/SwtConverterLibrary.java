package com.swtxml.converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.converter.SimpleTypeConverters.BooleanConverter;
import com.swtxml.converter.SimpleTypeConverters.CharacterConverter;
import com.swtxml.converter.SimpleTypeConverters.FloatConverter;
import com.swtxml.converter.SimpleTypeConverters.IntegerConverter;
import com.swtxml.converter.SimpleTypeConverters.StringConverter;

public class SwtConverterLibrary implements IConverterLibrary {

	private final Map<Class<?>, IConverter<?>> converters = new HashMap<Class<?>, IConverter<?>>();
	private final Map<PropertyNameAndClass, IConverter<?>> propertyNameConverters = new HashMap<PropertyNameAndClass, IConverter<?>>();

	private final static IConverterLibrary INSTANCE = new SwtConverterLibrary();

	public static IConverterLibrary getInstance() {
		return INSTANCE;
	}

	private SwtConverterLibrary() {
		converters.put(Color.class, new ColorConverter());
		BooleanConverter booleanConverter = new BooleanConverter();
		converters.put(Boolean.class, booleanConverter);
		converters.put(Boolean.TYPE, booleanConverter);
		IntegerConverter integerConverter = new IntegerConverter();
		converters.put(Integer.class, integerConverter);
		converters.put(Integer.TYPE, integerConverter);
		FloatConverter floatConverter = new FloatConverter();
		converters.put(Float.class, floatConverter);
		converters.put(Float.TYPE, floatConverter);
		CharacterConverter characterConverter = new CharacterConverter();
		converters.put(Character.class, characterConverter);
		converters.put(Character.TYPE, characterConverter);
		converters.put(String.class, new StringConverter());

		propertyNameConverters.put(new PropertyNameAndClass("style", Integer.TYPE),
				new StyleConverter());
		// TODO: only for rowlayout
		propertyNameConverters.put(new PropertyNameAndClass("type", Integer.TYPE),
				new StyleConverter());
		propertyNameConverters.put(new PropertyNameAndClass("layout", Layout.class),
				new LayoutConverter());
	}

	@SuppressWarnings("unchecked")
	public <T> IConverter<T> forProperty(String name, Class<T> clazz) {
		IConverter<T> converter = (IConverter<T>) propertyNameConverters
				.get(new PropertyNameAndClass(name, clazz));
		if (converter == null) {
			converter = (IConverter<T>) converters.get(clazz);
		}
		return converter;
	}

}
