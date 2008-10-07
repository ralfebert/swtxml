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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.Filters;
import com.swtxml.util.lang.IFilter;

public class FieldQuery {

	private Visibility visibility;
	private Subclasses subclasses;
	private List<IFilter<Field>> filters = new ArrayList<IFilter<Field>>();

	FieldQuery(Visibility visibility, Subclasses subclasses) {
		this.visibility = visibility;
		this.subclasses = subclasses;
	}

	private Collection<Field> getFields(Class<?> type) {
		if (visibility == Visibility.PUBLIC && subclasses == Subclasses.INCLUDE) {
			return Arrays.asList(type.getFields());
		} else if (visibility == Visibility.PUBLIC && subclasses == Subclasses.NONE) {
			List<Field> fields = Arrays.asList(type.getDeclaredFields());
			for (Iterator<Field> i = fields.iterator(); i.hasNext();) {
				if (!Modifier.isPublic(i.next().getModifiers())) {
					i.remove();
				}
			}
			return fields;

		} else if (visibility == Visibility.PRIVATE && subclasses == Subclasses.INCLUDE) {
			return getAllFields(type);
		}
		throw new UnsupportedOperationException("Querying with " + visibility + " and "
				+ subclasses + " not supported at the moment.");
	};

	public FieldQuery name(final String name) {
		filters.add(new IFilter<Field>() {
			public boolean match(Field field) {
				return field.getName().equals(name);
			}
		});
		return this;
	}

	public FieldQuery nameStartsWith(final String str) {
		filters.add(new IFilter<Field>() {
			public boolean match(Field field) {
				return field.getName().startsWith(str);
			}
		});
		return this;
	}

	public FieldQuery annotatedWith(final Class<? extends Annotation> annotationClass) {
		filters.add(new IFilter<Field>() {
			public boolean match(Field field) {
				return field.isAnnotationPresent(annotationClass);
			}
		});
		return this;
	}

	public FieldQuery type(final Class<?> type) {
		filters.add(new IFilter<Field>() {
			public boolean match(Field field) {
				return type.equals(field.getType());
			}
		});
		return this;
	}

	public FieldQuery isStatic(final boolean isStatic) {
		filters.add(new IFilter<Field>() {
			public boolean match(Field field) {
				return isStatic == Modifier.isStatic(field.getModifiers());
			}
		});
		return this;
	}

	public Collection<Field> all(Class<?> type) {
		return CollectionUtils.select(getFields(type), Filters.and(filters));
	}

	public Field exactOne(Class<?> type) {
		Collection<Field> results = all(type);
		if (results.size() == 1) {
			return results.iterator().next();
			// TODO: explain filter criteria in exception
		} else if (results.isEmpty()) {
			throw new ReflectorException("No suitable fields found for query!");
		} else {
			throw new ReflectorException("Ambiguous fields found for query!");
		}
	}

	/**
	 * Same as Class.getFields() but with private fields included. Returns all
	 * fields of <type> and its superclasses.
	 */
	private Collection<Field> getAllFields(Class<?> type) {
		List<Field> fields = new ArrayList<Field>();
		while (type != null) {
			for (Field field : type.getDeclaredFields()) {
				if (!field.isSynthetic()) {
					fields.add(field);
				}
			}
			type = type.getSuperclass();
		}
		return fields;
	}

}
