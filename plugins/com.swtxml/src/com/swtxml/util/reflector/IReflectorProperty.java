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

/**
 * IReflectorProperty implementations represent a bean property.
 * IReflectorProperty instances are managed by {@link ReflectorBean}. Was
 * introduced to represent public fields ( {@link ReflectorField}) in the same
 * way as getter+setter properties ( {@link ReflectorProperty}).
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface IReflectorProperty {

	public abstract String getName();

	public abstract Class<?> getType();

	public abstract Object get(Object obj);

	public abstract void set(Object obj, Object value);

}