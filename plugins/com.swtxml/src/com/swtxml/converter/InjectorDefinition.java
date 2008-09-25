package com.swtxml.converter;

import java.util.LinkedHashMap;
import java.util.Map;

import com.swtxml.util.IReflectorProperty;
import com.swtxml.util.ReflectorBean;

public class InjectorDefinition {

	private final LinkedHashMap<PropertyMatcher, ISetter> setters = new LinkedHashMap<PropertyMatcher, ISetter>();

	public <T> void addConverter(PropertyMatcher matcher, IConverter<T> converter) {
		addSetter(matcher, new ConvertingSetter(converter));
	}

	public void addSetter(PropertyMatcher matcher, ISetter setter) {
		setters.put(matcher, setter);
	}

	public IInjector getInjector(final Object obj, boolean includePublicFields) {
		final ReflectorBean bean = new ReflectorBean(obj.getClass(), includePublicFields);
		return new IInjector() {

			public void setPropertyValue(String name, String value) {
				for (PropertyMatcher matcher : setters.keySet()) {
					IReflectorProperty property = bean.getProperty(name);
					Class<?> type = property != null ? property.getType() : null;
					if (matcher.match(obj.getClass(), name, type)) {
						if (setters.get(matcher).apply(property, obj, name, value)) {
							return;
						}
					}
				}

			}

			public void setPropertyValues(Map<String, String> values) {
				for (String name : values.keySet()) {
					setPropertyValue(name, values.get(name));
				}
			}

		};
	}
}
