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

import org.apache.commons.lang.ObjectUtils;
import org.eclipse.core.runtime.Assert;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.Filters;
import com.swtxml.util.lang.IFilter;

public class MethodQuery {

	private Visibility visibility;
	private Subclasses subclasses;
	private List<IFilter<Method>> filters = new ArrayList<IFilter<Method>>();

	private static class AnyType {

	}

	public static final Class<?> ANY_TYPE = AnyType.class;

	MethodQuery(Visibility visibility, Subclasses subclasses) {
		this.visibility = visibility;
		this.subclasses = subclasses;
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
		filters.add(new IFilter<Method>() {
			public boolean match(Method method) {
				return method.getName().startsWith(str);
			}

			@Override
			public String toString() {
				return "name starts with \"" + str + "\"";
			}
		});
		return this;
	}

	public MethodQuery nameMatches(final String regex) {
		filters.add(new IFilter<Method>() {
			public boolean match(Method method) {
				return method.getName().matches(regex);
			}

			@Override
			public String toString() {
				return "name matches \"" + regex + "\"";
			}
		});
		return this;
	}

	public MethodQuery parameters(final Class<?>... signature) {
		filters.add(new IFilter<Method>() {
			public boolean match(Method method) {
				if (method.getParameterTypes().length != signature.length) {
					return false;
				}
				for (int i = 0; i < signature.length; i++) {
					if (AnyType.class.isAssignableFrom(signature[i])) {
						continue;
					} else if (!signature[i].isAssignableFrom(method.getParameterTypes()[i])) {
						return false;
					}
				}
				return true;
			}

			@Override
			public String toString() {
				return "parameters=\"" + Arrays.toString(signature) + "\"";
			}
		});
		return this;
	}

	public Collection<Method> all(Class<?> type) {
		return CollectionUtils.select(getMethods(type), getFilter());
	}

	private IFilter<Method> getFilter() {
		return Filters.and(filters);
	}

	public MethodQuery name(final String name) {
		filters.add(new IFilter<Method>() {
			public boolean match(Method method) {
				return method.getName().equals(name);
			}

			@Override
			public String toString() {
				return "name=\"" + name + "\"";
			}
		});
		return this;
	}

	public Method exactOne(Class<?> type) {
		Collection<Method> results = all(type);
		if (results.size() == 1) {
			return results.iterator().next();
		} else if (results.isEmpty()) {
			throw new ReflectorException("No method " + getFilter() + " found in " + type + "!");
		} else {
			throw new ReflectorException("Ambiguous methods found for " + getFilter() + " in "
					+ type + ": " + results);
		}
	}

	public MethodQuery optionalParameter(final Class<?> type) {
		Assert.isNotNull(type, "type");
		filters.add(new IFilter<Method>() {
			public boolean match(Method method) {
				return method.getParameterTypes().length == 0
						|| (method.getParameterTypes().length == 1 && type == method
								.getParameterTypes()[0]);

			}

			@Override
			public String toString() {
				return "optional parameter \"" + type.getSimpleName() + "\"";
			}
		});
		return this;
	}

	public MethodQuery returnType(final Class<?> type) {
		filters.add(new IFilter<Method>() {
			public boolean match(Method method) {
				return ObjectUtils.equals(type, method.getReturnType());
			}

			@Override
			public String toString() {
				return "return type \"" + (type != null ? type.getSimpleName() : "null") + "\"";
			}
		});
		return this;
	}

	/**
	 * Same as Class.getMethods() but with private methods included. Returns all
	 * methods of <type> and its superclasses, overwritten superclass methods
	 * are not included.
	 */
	private Collection<Method> getAllMethods(Class<?> type) {
		Map<String, Method> signatureToMethod = new HashMap<String, Method>();
		while (type != null) {
			for (Method method : type.getDeclaredMethods()) {
				if (method.isBridge() || method.isSynthetic()) {
					continue;
				}

				String signature = getSignature(method);
				if (!signatureToMethod.containsKey(signature)) {
					signatureToMethod.put(signature, method);
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
