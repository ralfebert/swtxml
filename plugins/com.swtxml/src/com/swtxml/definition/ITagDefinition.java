package com.swtxml.definition;

import java.util.Set;

public interface ITagDefinition {

	String getName();

	Set<String> getAttributeNames();

	public IAttributeDefinition getAttribute(String name);
}
