package com.swtxml.definition;

import java.util.Set;

public interface ITagDefinition {

	public static final ITagDefinition ROOT = new RootTagDefinition();

	String getName();

	Set<String> getAttributeNames();

	boolean isAllowedIn(ITagDefinition tagDefinition);

	public IAttributeDefinition getAttribute(String name);
}
