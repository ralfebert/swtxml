package com.swtxml.definition.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.swtxml.definition.INamespaceDefinition;

public class NamespaceDefinition implements INamespaceDefinition {

	private Map<String, TagDefinition> tags = new HashMap<String, TagDefinition>();

	public TagDefinition getTag(String name) {
		return tags.get(name);
	}

	public Set<String> getTagNames() {
		return Collections.unmodifiableSet(tags.keySet());
	}

	public TagDefinition defineTag(String name) {
		TagDefinition tagDefinition = new TagDefinition(name);
		tags.put(name, tagDefinition);
		return tagDefinition;
	}
}
