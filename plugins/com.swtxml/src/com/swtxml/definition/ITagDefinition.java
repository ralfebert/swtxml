package com.swtxml.definition;

import java.util.Map;

public interface ITagDefinition extends Comparable<ITagDefinition> {

	String getName();

	Map<String, ? extends IAttributeDefinition> getAttributes();

}
