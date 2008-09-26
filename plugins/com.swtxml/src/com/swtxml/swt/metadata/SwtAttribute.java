package com.swtxml.swt.metadata;

import com.swtxml.metadata.IAttribute;
import com.swtxml.util.reflector.IReflectorProperty;

public class SwtAttribute implements IAttribute {

	private final IReflectorProperty property;

	public SwtAttribute(IReflectorProperty property) {
		this.property = property;
	}

	public String getName() {
		return property.getName();
	}

	public IReflectorProperty getProperty() {
		return property;
	}

	@Override
	public String toString() {
		return "SwtTagAttribute[" + property.getName() + "]";
	}

	public int compareTo(IAttribute o) {
		return getName().compareTo(o.getName());
	}

	public <T> T adaptTo(Class<T> clazz) {
		return null;
	}

}
