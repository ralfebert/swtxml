/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.util.properties;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.reflector.ReflectorBean;
import com.swtxml.util.types.IType;

public class ClassProperties<A> {

	private Map<String, Property> properties = new HashMap<String, Property>();

	ClassProperties(ReflectorBean bean, Map<PropertyMatcher, IType<?>> propertyTypes) {
		for (IReflectorProperty prop : bean.getProperties()) {
			Property property = null;
			for (PropertyMatcher matcher : propertyTypes.keySet()) {
				if (matcher.match(bean.getType(), prop.getName(), prop.getType())) {
					property = new Property(prop, propertyTypes.get(matcher));
					break;
				}
			}
			if (property != null) {
				properties.put(property.getName(), property);
			}
		}
	}

	public IInjector getInjector(final Object obj) {
		return new IInjector() {

			public void setPropertyValue(String name, String value) {
				Property property = properties.get(name);
				if (property == null) {
					throw new PropertiesException("Unknown property \"" + name
							+ "\" (available are: "
							+ CollectionUtils.sortedToString(properties.keySet()) + ")");
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

	@Override
	public String toString() {
		return "ClassProperties[" + properties + "]";
	}

}
