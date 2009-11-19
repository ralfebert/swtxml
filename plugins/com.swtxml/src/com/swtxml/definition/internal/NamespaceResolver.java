/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.definition.internal;

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
