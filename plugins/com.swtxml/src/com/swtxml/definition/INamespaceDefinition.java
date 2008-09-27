package com.swtxml.definition;

import java.util.Set;

public interface INamespaceDefinition {

	public Set<String> getTagNames();

	public ITagDefinition getTag(String name);

}