package com.swtxml.definition;

import java.util.Map;

public interface INamespaceDefinition {

	public abstract Map<String, ? extends ITagDefinition> getTags();

}