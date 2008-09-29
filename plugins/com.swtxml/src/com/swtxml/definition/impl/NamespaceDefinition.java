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
package com.swtxml.definition.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;

public class NamespaceDefinition implements INamespaceDefinition {

	private Map<String, ITagDefinition> tags = new HashMap<String, ITagDefinition>();

	public ITagDefinition getTag(String name) {
		return tags.get(name);
	}

	public Set<String> getTagNames() {
		return Collections.unmodifiableSet(tags.keySet());
	}

	public TagDefinition defineTag(String name, ITagDefinition... allowedParentTags) {
		TagDefinition tagDefinition = new TagDefinition(name, allowedParentTags);
		tags.put(name, tagDefinition);
		return tagDefinition;
	}

	public void defineTag(ITagDefinition tag) {
		tags.put(tag.getName(), tag);
	}

}