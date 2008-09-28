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
package com.swtxml.util.properties;

import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.types.IType;

public class Property {

	private IReflectorProperty property;
	private IType<?> type;

	public IType<?> getType() {
		return type;
	}

	public Property(IReflectorProperty property, IType<?> type) {
		this.property = property;
		this.type = type;
	}

	public void set(Object obj, String value) {
		property.set(obj, type.convert(value));
	}

	public String getName() {
		return property.getName();
	}

	@Override
	public String toString() {
		return "Property[" + getName() + " " + getType() + "]";
	}

}
