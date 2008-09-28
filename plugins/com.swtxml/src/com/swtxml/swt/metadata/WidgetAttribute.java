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
package com.swtxml.swt.metadata;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.util.properties.Property;
import com.swtxml.util.types.IType;

public class WidgetAttribute implements IAttributeDefinition {

	private Property property;

	public WidgetAttribute(Property property) {
		this.property = property;
	}

	public String getName() {
		return property.getName();
	}

	@Override
	public String toString() {
		return "SwtTagAttribute[" + property.getName() + "]";
	}

	public int compareTo(IAttributeDefinition o) {
		return getName().compareTo(o.getName());
	}

	public IType<?> getType() {
		return property.getType();
	}

}
