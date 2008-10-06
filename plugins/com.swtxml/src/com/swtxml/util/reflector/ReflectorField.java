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

import java.lang.reflect.Field;

class ReflectorField implements IReflectorProperty {

	private Field field;

	public ReflectorField(Field field) {
		this.field = field;
	}

	public Object get(Object obj) {
		try {
			return field.get(obj);
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	public String getName() {
		return field.getName();
	}

	public Class<?> getType() {
		return field.getType();
	}

	public void set(Object obj, Object value) {
		try {
			field.set(obj, value);
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	@Override
	public String toString() {
		return "ReflectorField[" + getType() + " " + getName() + "]";
	}

}
