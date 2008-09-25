package com.swtxml.metadata;

import com.swtxml.util.ReflectorProperty;

public class SwtTagAttribute implements ITagAttribute {

	private ReflectorProperty property;

	public SwtTagAttribute(ReflectorProperty property) {
		this.property = property;
	}

	public String getName() {
		return property.getName();
	}

	@Override
	public String toString() {
		return "SwtTagAttribute[" + property.getName() + "]";
	}

	public int compareTo(ITagAttribute o) {
		return getName().compareTo(o.getName());
	}

	@SuppressWarnings("unchecked")
	public <T> T adaptTo(Class<T> clazz) {
		if (clazz.isAssignableFrom(SwtAttributeSetter.class)) {
			return (T) new SwtAttributeSetter(this);
		}
		return null;
	}

}
