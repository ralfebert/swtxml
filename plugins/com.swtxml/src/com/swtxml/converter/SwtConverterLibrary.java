package com.swtxml.converter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.converter.SimpleTypeConverters.BooleanConverter;
import com.swtxml.converter.SimpleTypeConverters.CharacterConverter;
import com.swtxml.converter.SimpleTypeConverters.FloatConverter;
import com.swtxml.converter.SimpleTypeConverters.IntegerConverter;
import com.swtxml.converter.SimpleTypeConverters.StringConverter;

public class SwtConverterLibrary implements IConverterLibrary {

	private final List<PropertyMatcher> matchers = new ArrayList<PropertyMatcher>();

	private final static IConverterLibrary INSTANCE = new SwtConverterLibrary();

	public static IConverterLibrary getInstance() {
		return INSTANCE;
	}

	private SwtConverterLibrary() {
		Class<?> ALL_CLASSES = null;
		String ALL_NAMES = null;

		matcher(new StyleConverter(), Widget.class, "style", Integer.TYPE);
		matcher(new StyleConverter(), Layout.class, "type", Integer.TYPE);
		matcher(new LayoutConverter(), Composite.class, "layout", Layout.class);

		matcher(new ColorConverter(), ALL_CLASSES, ALL_NAMES, Color.class);
		matcher(new BooleanConverter(), ALL_CLASSES, ALL_NAMES, Boolean.class, Boolean.TYPE);
		matcher(new IntegerConverter(), ALL_CLASSES, ALL_NAMES, Integer.class, Integer.TYPE);
		matcher(new FloatConverter(), ALL_CLASSES, ALL_NAMES, Float.class, Float.TYPE);
		matcher(new CharacterConverter(), ALL_CLASSES, ALL_NAMES, Character.class, Character.TYPE);
		matcher(new StringConverter(), ALL_CLASSES, ALL_NAMES, String.class);
	}

	private void matcher(IConverter<?> converter, Class<?> forClass, String propertyName,
			Class<?>... propertyTypes) {
		matchers.add(new PropertyMatcher(converter, forClass, propertyName, propertyTypes));
	}

	@SuppressWarnings("unchecked")
	public <T> IConverter<T> forProperty(Object obj, String propertyName, Class<T> targetType) {
		for (PropertyMatcher matcher : matchers) {
			if (matcher.match(obj, propertyName, targetType)) {
				return (IConverter<T>) matcher.getConverter();
			}
		}
		return null;
	}

}
