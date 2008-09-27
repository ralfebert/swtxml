package com.swtxml.parser;

import com.swtxml.definition.INamespaceDefinition;

public interface INamespaceResolver {

	INamespaceDefinition resolveNamespace(String uri);

}
