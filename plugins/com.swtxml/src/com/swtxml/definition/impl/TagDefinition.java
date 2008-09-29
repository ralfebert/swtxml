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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.util.types.IType;

public class TagDefinition implements ITagDefinition {

	private final String name;
	private final Map<String, IAttributeDefinition> attributes = new HashMap<String, IAttributeDefinition>();
	private Set<ITagDefinition> allowedParentTags;

	public TagDefinition(String name, ITagDefinition... allowedParentTags) {
		this.name = name;
		this.allowedParentTags = new HashSet<ITagDefinition>(Arrays.asList(allowedParentTags));
	}

	public IAttributeDefinition getAttribute(String name) {
		return attributes.get(name);
	}

	public Set<String> getAttributeNames() {
		return Collections.unmodifiableSet(attributes.keySet());
	}

	public String getName() {
		return name;
	}

	public AttributeDefinition defineAttribute(String name, IType<?> type) {
		AttributeDefinition attributeDefinition = new AttributeDefinition(name, type);
		attributes.put(name, attributeDefinition);
		return attributeDefinition;
	}

	public boolean isAllowedIn(ITagDefinition parentTagDefinition) {
		return allowedParentTags.contains(parentTagDefinition);
	}

	public TagDefinition allowNested() {
		allowedParentTags.add(this);
		return this;
	}
}
