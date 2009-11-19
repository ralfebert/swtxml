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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.ITagScope;

public class TagDefinition implements ITagDefinition, ITagScope {

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

	public void defineAttribute(IAttributeDefinition attributeDefinition) {
		attributes.put(attributeDefinition.getName(), attributeDefinition);
	}

	public boolean isAllowedIn(ITagDefinition parentTagDefinition) {
		return allowedParentTags.contains(parentTagDefinition);
	}

	public TagDefinition allowNested() {
		allowedParentTags.add(this);
		return this;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name + "]";
	}
}
