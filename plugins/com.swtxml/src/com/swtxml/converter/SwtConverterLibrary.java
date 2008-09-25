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
		Class<?> allClasses = null;
		String allProperties = null;

		matcher(new StyleConverter(), Widget.class, "style", Integer.TYPE);
		matcher(new StyleConverter(), Layout.class, "type", Integer.TYPE);
		matcher(new LayoutConverter(), Composite.class, "layout", Layout.class);

		matcher(new ColorConverter(), allClasses, allProperties, Color.class);
		matcher(new BooleanConverter(), allClasses, allProperties, Boolean.class, Boolean.TYPE);
		matcher(new IntegerConverter(), allClasses, allProperties, Integer.class, Integer.TYPE);
		matcher(new FloatConverter(), allClasses, allProperties, Float.class, Float.TYPE);
		matcher(new CharacterConverter(), allClasses, allProperties, Character.class,
				Character.TYPE);
		matcher(new StringConverter(), allClasses, allProperties, String.class);
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
