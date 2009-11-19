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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.swtxml.definition.DefinitionException;
import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;

public class NamespaceDefinition implements INamespaceDefinition {

	private Map<String, ITagDefinition> tagsByName = new HashMap<String, ITagDefinition>();
	private Map<String, IAttributeDefinition> foreignAttributes = new HashMap<String, IAttributeDefinition>();
	private String uri;

	public NamespaceDefinition(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public ITagDefinition getTag(String name) {
		return tagsByName.get(name);
	}

	public Set<String> getTagNames() {
		return Collections.unmodifiableSet(tagsByName.keySet());
	}

	public void defineTag(ITagDefinition tag) {
		ITagDefinition existingTag = tagsByName.get(tag.getName());
		if (existingTag != null) {
			throw new DefinitionException("Tag naming conflict between " + tag + " and "
					+ existingTag + "!");
		}

		tagsByName.put(tag.getName(), tag);
	}

	public void defineForeignAttribute(IAttributeDefinition attr) {
		foreignAttributes.put(attr.getName(), attr);
	}

	public Set<String> getForeignAttributeNames() {
		return Collections.unmodifiableSet(foreignAttributes.keySet());
	}

	public IAttributeDefinition getForeignAttribute(String name) {
		return foreignAttributes.get(name);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + tagsByName + ", " + foreignAttributes + "]";
	}

}