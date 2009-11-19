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
import java.util.HashSet;
import java.util.Set;

import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.ITagScope;
import com.swtxml.util.types.IType;

public class ForeignAttributeDefinition extends AttributeDefinition implements ITagScope {

	private final Set<ITagDefinition> allowedParentTags;

	public ForeignAttributeDefinition(String name, IType<?> type,
			ITagDefinition... allowedParentTags) {
		super(name, type);
		this.allowedParentTags = new HashSet<ITagDefinition>(Arrays.asList(allowedParentTags));
	}

	public boolean isAllowedIn(ITagDefinition parentTagDefinition) {
		return allowedParentTags.contains(parentTagDefinition);
	}

}