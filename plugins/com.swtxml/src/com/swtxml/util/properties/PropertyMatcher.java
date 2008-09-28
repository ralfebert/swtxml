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
package com.swtxml.util.properties;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

public class PropertyMatcher {

	public static final Class<?> ALL_CLASSES = null;
	public static final String ALL_PROPERTIES = null;

	private final Class<?> forClass;
	private final String propertyName;
	private final Class<?>[] targetTypes;

	public PropertyMatcher(Class<?>... propertyTypes) {
		this(ALL_CLASSES, ALL_PROPERTIES, propertyTypes);
	}

	public PropertyMatcher(Class<?> forClass, String propertyName, Class<?>... propertyTypes) {
		super();
		this.forClass = forClass;
		this.propertyName = propertyName;
		this.targetTypes = propertyTypes;
		for (Class<?> type : propertyTypes) {
			if (type == null) {
				throw new PropertiesException("propertyTypes may not be null");
			}
		}
	}

	public boolean match(Class<?> queryClass, String queryPropertyName, Class<?> queryTargetType) {
		if (forClass != null && !forClass.isAssignableFrom(queryClass)) {
			return false;
		}
		if (propertyName != null && !propertyName.equals(queryPropertyName)) {
			return false;
		}
		if (targetTypes.length > 0 && !ArrayUtils.contains(targetTypes, queryTargetType)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String classLabel = forClass != null ? forClass.getSimpleName() : "*";
		String propLabel = propertyName != null ? propertyName : "*";
		String targetTypesLabel = targetTypes.length > 0 ? Arrays.toString(targetTypes) : "*";
		return "PropertyMatcher[" + classLabel + "." + propLabel + " = " + targetTypesLabel + "]";
	}

}