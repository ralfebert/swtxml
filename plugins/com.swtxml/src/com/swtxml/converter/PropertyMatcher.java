package com.swtxml.converter;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;

public class PropertyMatcher {

	public static final Class<?> ALL_CLASSES = null;
	public static final String ALL_PROPERTIES = null;

	private final ISetter<?> setter;

	private final Class<?> forClass;
	private final String propertyName;
	private final Class<?>[] targetTypes;

	public PropertyMatcher(ISetter<?> setter, Class<?> forClass, String propertyName,
			Class<?>... propertyTypes) {
		super();
		this.setter = setter;
		this.forClass = forClass;
		this.propertyName = propertyName;
		this.targetTypes = propertyTypes;
	}

	public ISetter<?> getSetter() {
		return setter;
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