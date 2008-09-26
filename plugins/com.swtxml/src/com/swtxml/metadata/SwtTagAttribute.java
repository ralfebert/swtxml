package com.swtxml.metadata;

import com.swtxml.util.reflector.IReflectorProperty;

public class SwtTagAttribute implements ITagAttribute {

	private final IReflectorProperty property;

	public SwtTagAttribute(IReflectorProperty property) {
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

	public int compareTo(ITagAttribute o) {
		return getName().compareTo(o.getName());
	}

	public <T> T adaptTo(Class<T> clazz) {
		return null;
	}

}
