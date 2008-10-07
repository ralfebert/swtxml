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

import org.apache.commons.lang.StringUtils;

import com.swtxml.util.reflector.ReflectorException;

public class CollectionUtils {

	/**
	 * Returns the first element from iterable for which filter.match(element)
	 * returned true.
	 */
	public static <A> A find(Iterable<? extends A> iterable, IFilter<A> filter) {
		for (A a : iterable) {
			if (filter.match(a)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * Returns a new collection containing all elements from collection for
	 * which filter.match(element) returned true.
	 */
	@SuppressWarnings("unchecked")
	public static <A> Collection<A> select(Collection<? extends A> collection, IFilter<A> filter) {
		Collection<A> resultList = createCollection(collection);
		for (A a : collection) {
			if (filter.match(a)) {
				resultList.add(a);
			}
		}
		return resultList;
	}

	/**
	 * Returns a new collection containing the result from
	 * function.apply(element) for all elements from collection.
	 */
	@SuppressWarnings("unchecked")
	public static <FROM, TO> Collection<TO> collect(Collection<? extends FROM> collection,
			IFunction<FROM, TO> function) {
		Collection<TO> resultList = createCollection(collection);
		for (FROM a : collection) {
			resultList.add(function.apply(a));
		}
		return resultList;
	}

	/**
	 * Returns a new list containing the result from function.apply(element) for
	 * all elements from list.
	 */
	public static <FROM, TO> List<TO> collect(List<? extends FROM> list,
			IFunction<FROM, TO> function) {
		List<TO> resultList = new ArrayList<TO>();
		for (FROM a : list) {
			resultList.add(function.apply(a));
		}
		return resultList;
	}

	/**
	 * Returns a comma-separated String of the collection toString values
	 * alphabetically sorted by value.
	 */
	public static String sortedToString(Collection<?> collection) {
		List<String> strings = new ArrayList<String>(collect(collection, Functions.TO_STRING));
		Collections.sort(strings);
		return StringUtils.join(strings, ", ");
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

}