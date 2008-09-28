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
package com.swtxml.util.reflector;

import java.util.Collection;
import java.util.Collections;

import com.swtxml.util.lang.ContractProof;

public class ReflectorBean {

	private Collection<IReflectorProperty> properties;

	public Class<?> getType() {
		return type;
	}

	public ReflectorBean(Class<?> type) {
		this(type, false);
	}

	public ReflectorBean(Class<?> type, boolean includePublicFields) {
		ContractProof.notNull(type, "type");
		this.type = type;
		this.properties = Reflector.findPublicProperties(type, includePublicFields);
	}

	public IReflectorProperty getProperty(String propertyName) {
		for (IReflectorProperty property : properties) {
			if (propertyName.equals(property.getName())) {
				return property;
			}
		}
		return null;
	}

	public Collection<IReflectorProperty> getProperties() {
		return Collections.unmodifiableCollection(properties);
	}

	@Override
	public String toString() {
		return "ReflectorBean[" + getName() + "]";
	}

	public String getName() {
		return type.getSimpleName();
	}

	private final Class<?> type;

}
