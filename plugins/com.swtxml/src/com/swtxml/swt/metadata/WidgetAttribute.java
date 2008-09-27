package com.swtxml.swt.metadata;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.util.properties.Property;
import com.swtxml.util.types.IType;

public class WidgetAttribute implements IAttributeDefinition {

	private Property property;

	public WidgetAttribute(Property property) {
		this.property = property;
	}

	public String getName() {
		return property.getName();
	}

	@Override
	public String toString() {
		return "SwtTagAttribute[" + property.getName() + "]";
	}

	public int compareTo(IAttributeDefinition o) {
		return getName().compareTo(o.getName());
	}

	public IType<?> getType() {
		return property.getType();
	}

}
