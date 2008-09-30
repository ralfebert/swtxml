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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.swtxml.util.reflector.MethodQuery.Subclasses;
import com.swtxml.util.reflector.MethodQuery.Visibility;

public class Reflector {

	public static MethodQuery findMethods(Visibility visibility, Subclasses subclasses) {
		return new MethodQuery(visibility, subclasses);
	}

	public static Collection<Method> findPublicSetters(Class<?> cl) {
		return findMethods(Visibility.PUBLIC, Subclasses.INCLUDE).nameStartsWith("set").parameters(
				MethodQuery.ANY_TYPE).all(cl);
	}

	public static Collection<IReflectorProperty> findPublicProperties(Class<?> cl) {
		return findPublicProperties(cl, false);
	}

	public static Collection<IReflectorProperty> findPublicProperties(Class<?> cl,
			boolean includePublicFields) {
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

		if (includePublicFields) {
			List<Field> fields = Arrays.asList(cl.getFields());
			for (Field f : fields) {
				if (!Modifier.isStatic(f.getModifiers())) {
					properties.add(new ReflectorField(f));
				}
			}
		}

		return properties;
	}

}
