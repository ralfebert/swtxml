package com.swtxml.util.properties;

import com.swtxml.util.reflector.IReflectorProperty;

public class Property {

	private IReflectorProperty property;
	private ISetter setter;

	public Property(IReflectorProperty property, ISetter setter) {
		this.property = property;
		this.setter = setter;
	}

	public void set(Object obj, String value) {
		setter.apply(property, obj, property.getName(), value);
	}

	public String getName() {
		return property.getName();
	}

}
