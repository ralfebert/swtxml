package com.swtxml.util.properties;

import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.types.IType;

public class Property {

	private IReflectorProperty property;
	private IType<?> type;

	public IType<?> getType() {
		return type;
	}

	public Property(IReflectorProperty property, IType<?> type) {
		this.property = property;
		this.type = type;
	}

	public void set(Object obj, String value) {
		property.set(obj, type.convert(obj, value));
	}

	public String getName() {
		return property.getName();
	}

}
