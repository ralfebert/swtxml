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

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.util.types.IType;

public class AttributeDefinition implements IAttributeDefinition {

	private String name;
	private IType<?> type;

	public AttributeDefinition(String name, IType<?> type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IType<?> getType() {
		return type;
	}

	public void setType(IType<?> type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + name + ", " + type + "]";
	}

}