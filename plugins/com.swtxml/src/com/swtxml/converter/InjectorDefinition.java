package com.swtxml.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.swtxml.util.IReflectorProperty;
import com.swtxml.util.ReflectorBean;
import com.swtxml.util.ReflectorException;

public class InjectorDefinition {

	private final List<PropertyMatcher> matchers = new ArrayList<PropertyMatcher>();

	public <T> void addConverter(IConverter<T> converter, Class<?> forClass, String propertyName,
			Class<?>... propertyTypes) {
		addSetter(new ConvertingSetter<T>(converter), forClass, propertyName, propertyTypes);
	}

	public void addSetter(ISetter<?> setter, Class<?> forClass, String propertyName,
			Class<?>... propertyTypes) {
		matchers.add(new PropertyMatcher(setter, forClass, propertyName, propertyTypes));
	}

	public IInjector getInjector(final Object obj, boolean includePublicFields) {
		final ReflectorBean bean = new ReflectorBean(obj.getClass(), includePublicFields);
		return new IInjector() {

			public void setPropertyValue(String name, String value) {
				IReflectorProperty property = bean.getProperty(name);
				if (property == null) {
					throw new ReflectorException("Property " + name + " not found in "
							+ bean.getName());
				}
				ISetter<?> setter = forProperty(obj.getClass(), name, property.getType());
				if (setter == null) {
					throw new ReflectorException("No setter found for property " + name + " in "
							+ bean.getName());
				}
				setter.set(obj, property, value);
			}

			public void setPropertyValues(Map<String, String> values) {
				for (String name : values.keySet()) {
					setPropertyValue(name, values.get(name));
				}
			}

		};
	}

	@SuppressWarnings("unchecked")
	private <T> ISetter<T> forProperty(Class<?> clazz, String propertyName, Class<T> targetType) {
		for (PropertyMatcher matcher : matchers) {
			if (matcher.match(clazz, propertyName, targetType)) {
				return (ISetter<T>) matcher.getSetter();
			}
		}
		return null;
	}
}
