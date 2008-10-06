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

import java.lang.reflect.Method;

class ReflectorProperty implements IReflectorProperty {

	private final Method getter;
	private final Method setter;

	public ReflectorProperty(Method getter, Method setter) {
		this.getter = getter;
		this.setter = setter;
	}

	public String getName() {
		String n = getter.getName().substring(3);
		return n.substring(0, 1).toLowerCase() + n.substring(1);
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}

	public void set(Object obj, Object value) {
		try {
			setter.invoke(obj, value);
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	public Class<?> getType() {
		return getter.getReturnType();
	}

	public Object get(Object obj) {
		try {
			return getter.invoke(obj);
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	@Override
	public String toString() {
		return "ReflectorProperty[" + getType() + " " + getName() + "]";
	}

}