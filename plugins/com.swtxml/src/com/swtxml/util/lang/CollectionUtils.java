package com.swtxml.util.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		if (original instanceof List) {
			return new ArrayList();
		}
		if (original instanceof Set) {
			return new HashSet();
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

}
