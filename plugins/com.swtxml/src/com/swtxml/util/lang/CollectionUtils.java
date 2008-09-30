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
package com.swtxml.util.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.swtxml.util.reflector.ReflectorException;

public class CollectionUtils {

	public static <A> A find(Iterable<? extends A> iterable, IPredicate<A> predicate) {
		for (A a : iterable) {
			if (predicate.match(a)) {
				return a;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <A> Collection<A> select(Collection<? extends A> collection,
			IPredicate<A> predicate) {
		Collection<A> resultList = createCollection(collection);
		for (A a : collection) {
			if (predicate.match(a)) {
				resultList.add(a);
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	private static Collection createCollection(Collection original) {
		if (original instanceof Set) {
			return new HashSet();
		}
		if (original instanceof Collection) {
			return new ArrayList();
		}
		throw new ReflectorException("Unknown collection type: " + original.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <FROM, TO> Collection<TO> collect(Collection<? extends FROM> collection,
			IFunction<FROM, TO> function) {
		Collection<TO> resultList = createCollection(collection);
		for (FROM a : collection) {
			resultList.add(function.apply(a));
		}
		return resultList;
	}

	public static <FROM, TO> List<TO> collect(List<? extends FROM> list,
			IFunction<FROM, TO> function) {
		List<TO> resultList = new ArrayList<TO>();
		for (FROM a : list) {
			resultList.add(function.apply(a));
		}
		return resultList;
	}

	/**
	 * Returns a comma-separated String of the collection values alphabetically
	 * sorted by value.
	 */
	public static String sortedToString(Collection<?> collection) {
		List<String> strings = new ArrayList<String>(collect(collection,
				new IFunction<Object, String>() {

					public String apply(Object obj) {
						return ObjectUtils.toString(obj);
					}

				}));
		Collections.sort(strings);
		return StringUtils.join(strings, ", ");
	}

	public static <A> IPredicate<A> and(final Iterable<IPredicate<A>> predicates) {
		return new IPredicate<A>() {
			public boolean match(A obj) {
				for (IPredicate<A> predicate : predicates) {
					if (!predicate.match(obj)) {
						return false;
					}
				}
				return true;
			}
		};
	}

}
