package com.swtxml.definition;

import java.util.Set;

//TODO: throw out comparable and compare by name
public interface ITagDefinition extends Comparable<ITagDefinition> {

	String getName();

	Set<String> getAttributeNames();

	public IAttributeDefinition getAttribute(String name);
}
