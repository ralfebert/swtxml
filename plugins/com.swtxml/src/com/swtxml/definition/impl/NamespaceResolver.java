package com.swtxml.definition.impl;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.INamespaceResolver;

public class NamespaceResolver implements INamespaceResolver {

	private Map<String, INamespaceDefinition> namespaces = new HashMap<String, INamespaceDefinition>();

	public INamespaceDefinition resolveNamespace(String uri) {
		return namespaces.get(uri);
	}

	public void addNamespace(String uri, INamespaceDefinition namespaceDefinition) {
		namespaces.put(uri, namespaceDefinition);
	}

}
