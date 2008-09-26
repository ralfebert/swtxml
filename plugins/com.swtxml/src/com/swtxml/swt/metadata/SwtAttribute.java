package com.swtxml.swt.metadata;

import com.swtxml.metadata.IAttribute;
import com.swtxml.util.properties.Property;
import com.swtxml.util.types.IType;

public class SwtAttribute implements IAttribute {

	private Property property;

	public SwtAttribute(Property property) {
		this.property = property;
	}

	public String getName() {
		return property.getName();
	}

	@Override
	public String toString() {
		return "SwtTagAttribute[" + property.getName() + "]";
	}

	public int compareTo(IAttribute o) {
		return getName().compareTo(o.getName());
	}

	public IType<?> getType() {
		return property.getType();
	}

}
