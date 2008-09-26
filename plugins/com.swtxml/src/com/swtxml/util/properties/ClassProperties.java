package com.swtxml.util.properties;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.reflector.ReflectorBean;

public class ClassProperties<A> {

	private Map<String, Property> properties = new HashMap<String, Property>();

	ClassProperties(ReflectorBean bean, Map<PropertyMatcher, ISetter> setters) {
		for (IReflectorProperty prop : bean.getProperties()) {
			for (PropertyMatcher matcher : setters.keySet()) {
				if (matcher.match(bean.getType(), prop.getName(), prop.getType())) {
					properties.put(prop.getName(), new Property(prop, setters.get(matcher)));
					break;
				}
			}
		}
	}

	public IInjector getInjector(final Object obj) {
		return new IInjector() {

			public void setPropertyValue(String name, String value) {
				Property property = properties.get(name);
				if (property == null) {
					throw new PropertiesException("Property " + name + " not found!");
				}
				property.set(obj, value);
			}

			public void setPropertyValues(Map<String, String> values) {
				for (String name : values.keySet()) {
					setPropertyValue(name, values.get(name));
				}
			}

		};
	}

	public Map<String, Property> getProperties() {
		return properties;
	}

}
