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

import org.eclipse.core.runtime.Assert;

/**
 * ReflectorBean represents all properties (getter+setter or public field) of a
 * java bean class.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class ReflectorBean {

	private Collection<IReflectorProperty> properties;

	public Class<?> getType() {
		return type;
	}

	public ReflectorBean(Class<?> type, PublicFields publicFields) {
		Assert.isNotNull(type, "type");
		this.type = type;
		this.properties = Collections.unmodifiableCollection(Reflector.findPublicProperties(type,
				publicFields));
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
		return properties;
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
