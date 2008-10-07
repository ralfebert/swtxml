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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Reflector is a helper component to inspect classes using Java Reflection more
 * conveniently.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class Reflector {

	public static MethodQuery findMethods(Visibility visibility, Subclasses subclasses) {
		return new MethodQuery(visibility, subclasses);
	}

	public static FieldQuery findFields(Visibility visibility, Subclasses subclasses) {
		return new FieldQuery(visibility, subclasses);
	}

	static Collection<Method> findPublicSetters(Class<?> cl) {
		return findMethods(Visibility.PUBLIC, Subclasses.INCLUDE).nameStartsWith("set").parameters(
				MethodQuery.ANY_TYPE).all(cl);
	}

	public static Collection<IReflectorProperty> findPublicProperties(Class<?> cl,
			PublicFields publicFields) {
		Collection<IReflectorProperty> properties = new ArrayList<IReflectorProperty>();
		Collection<Method> setters = findPublicSetters(cl);
		for (final Method setter : setters) {
			Collection<Method> getters = Reflector.findMethods(Visibility.PUBLIC,
					Subclasses.INCLUDE).name("g" + setter.getName().substring(1)).parameters()
					.returnType(setter.getParameterTypes()[0]).all(cl);
			if (getters.size() == 1) {
				properties.add(new ReflectorProperty(getters.iterator().next(), setter));
			}
		}

		if (PublicFields.INCLUDE == publicFields) {
			Collection<Field> fields = Reflector.findFields(Visibility.PUBLIC, Subclasses.INCLUDE)
					.isStatic(false).all(cl);
			for (Field f : fields) {
				properties.add(new ReflectorField(f));
			}
		}

		return properties;
	}

}
