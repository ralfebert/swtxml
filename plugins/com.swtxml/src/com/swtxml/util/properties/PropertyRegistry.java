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

import java.util.LinkedHashMap;

import com.swtxml.util.reflector.PublicFields;
import com.swtxml.util.reflector.ReflectorBean;
import com.swtxml.util.types.IType;

public class PropertyRegistry {

	private LinkedHashMap<PropertyMatcher, IType<?>> types = new LinkedHashMap<PropertyMatcher, IType<?>>();
	private PublicFields publicFields;

	public PropertyRegistry(PublicFields publicFields) {
		this.publicFields = publicFields;
	}

	public <T> void add(PropertyMatcher matcher, IType<T> type) {
		types.put(matcher, type);
	}

	public <A> ClassProperties<A> getProperties(Class<A> clazz) {
		return new ClassProperties<A>(new ReflectorBean(clazz, publicFields), types);
	}
}
