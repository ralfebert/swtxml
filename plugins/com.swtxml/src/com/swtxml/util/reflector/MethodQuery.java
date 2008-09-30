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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IPredicate;

public class MethodQuery {

	private Visibility visibility;
	private Subclasses subclasses;
	private List<IPredicate<Method>> predicates = new ArrayList<IPredicate<Method>>();

	private static class AnyType {

	}

	public static final Class<?> ANY_TYPE = AnyType.class;

	MethodQuery(Visibility visibility, Subclasses subclasses) {
		this.visibility = visibility;
		this.subclasses = subclasses;
	}

	public static enum Visibility {
		PUBLIC, PRIVATE
	};

	public static enum Subclasses {
		INCLUDE, NONE
	}

	private Collection<Method> getMethods(Class<?> type) {
		if (visibility == Visibility.PUBLIC && subclasses == Subclasses.INCLUDE) {
			return Arrays.asList(type.getMethods());
		} else if (visibility == Visibility.PRIVATE && subclasses == Subclasses.INCLUDE) {
			return getAllMethods(type);
		}
		throw new UnsupportedOperationException("Querying with " + visibility + " and "
				+ subclasses + " not supported at the moment.");
	};

	public MethodQuery nameStartsWith(final String str) {
		predicates.add(new IPredicate<Method>() {
			public boolean match(Method obj) {
				return obj.getName().startsWith(str);
			}
		});
		return this;
	}

	public MethodQuery parameters(final Class<?>... signature) {
		predicates.add(new IPredicate<Method>() {
			public boolean match(Method obj) {
				if (obj.getParameterTypes().length != signature.length) {
					return false;
				}
				for (int i = 0; i < signature.length; i++) {
					if (AnyType.class.isAssignableFrom(signature[i])) {
						continue;
					} else if (!signature[i].isAssignableFrom(obj.getParameterTypes()[i])) {
						return false;
					}
				}
				return true;
			}
		});
		return this;
	}

	public Collection<Method> all(Class<?> type) {
		return CollectionUtils.select(getMethods(type), CollectionUtils.and(predicates));
	}

	public MethodQuery name(final String name) {
		predicates.add(new IPredicate<Method>() {
			public boolean match(Method m) {
				return m.getName().equals(name);
			}
		});
		return this;
	}

	public Method exactOne(Class<?> type) {
		Collection<Method> results = all(type);
		if (results.size() == 1) {
			return results.iterator().next();
			// TODO: explain filter criteria in exception
		} else if (results.isEmpty()) {
			throw new ReflectorException("No suitable method found for query!");
		} else {
			throw new ReflectorException("Ambiguous methods found for query!");
		}
	}

	public MethodQuery optionalParameter(final Class<?> type) {
		predicates.add(new IPredicate<Method>() {
			public boolean match(Method m) {
				return m.getParameterTypes().length == 0
						|| (m.getParameterTypes().length == 1 && type == m.getParameterTypes()[0]);

			}
		});
		return this;
	}

	public MethodQuery returnType(final Class<?> type) {
		predicates.add(new IPredicate<Method>() {
			public boolean match(Method m) {
				return type.equals(m.getReturnType());
			}
		});
		return this;
	}

	/**
	 * Returns all methods of <type>, overwritten methods are included only
	 * once. Same as Class.getMethods() only with private methods included.
	 */
	private Collection<Method> getAllMethods(Class<?> type) {
		Map<String, Method> signatureToMethod = new HashMap<String, Method>();
		while (type != null) {
			for (Method currentMethod : type.getDeclaredMethods()) {
				if (currentMethod.isBridge()) {
					continue;
				}

				String signature = getSignature(currentMethod);
				if (!signatureToMethod.containsKey(signature)) {
					signatureToMethod.put(signature, currentMethod);
				}
			}
			type = type.getSuperclass();
		}

		return signatureToMethod.values();
	}

	private String getSignature(Method method) {
		StringBuffer s = new StringBuffer();
		s.append(method.getName());

		for (Class<?> param : method.getParameterTypes()) {
			s.append(':');
			s.append(param.getName());
		}

		return s.toString();
	}
}
