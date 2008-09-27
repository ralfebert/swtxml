package com.swtxml.definition.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.util.types.IType;

public class TagDefinition implements ITagDefinition {

	private final String name;
	private final Map<String, AttributeDefinition> attributes = new HashMap<String, AttributeDefinition>();

	public TagDefinition(String name) {
		this.name = name;
	}

	public IAttributeDefinition getAttribute(String name) {
		return attributes.get(name);
	}

	public Set<String> getAttributeNames() {
		return Collections.unmodifiableSet(attributes.keySet());
	}

	public String getName() {
		return name;
	}

	public int compareTo(ITagDefinition o) {
		return name.compareTo(o.getName());
	}

	public AttributeDefinition defineAttribute(String name, IType<?> type) {
		AttributeDefinition attributeDefinition = new AttributeDefinition(name, type);
		attributes.put(name, attributeDefinition);
		return attributeDefinition;
	}

}
