package com.swtxml.util.properties;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.reflector.ReflectorBean;

public class ClassProperties<A> {

	private Map<String, Property> properties = new HashMap<String, Property>();

	ClassProperties(ReflectorBean bean, Map<PropertyMatcher, IType<?>> propertyTypes) {
		for (IReflectorProperty prop : bean.getProperties()) {
			// System.out.println("\n\n\n======== " + prop.getName());
			Property property = null;
			for (PropertyMatcher matcher : propertyTypes.keySet()) {
				if (matcher.match(bean.getType(), prop.getName(), prop.getType())) {
					property = new Property(prop, propertyTypes.get(matcher));
					// System.out.println("  > " + matcher);
					break;
				}
				// System.out.println("    " + matcher);
			}
			if (property != null) {
				properties.put(property.getName(), property);
			} else {
				System.out.println("No type found for: " + prop + ", ignored");
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
