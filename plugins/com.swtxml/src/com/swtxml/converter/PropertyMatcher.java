package com.swtxml.converter;

import org.apache.commons.lang.ArrayUtils;

public class PropertyMatcher {

	private final IConverter<?> converter;

	private final Class<?> forClass;
	private final String propertyName;
	private final Class<?>[] targetTypes;

	public PropertyMatcher(IConverter<?> converter, Class<?> forClass, String propertyName,
			Class<?>... propertyTypes) {
		super();
		this.converter = converter;
		this.forClass = forClass;
		this.propertyName = propertyName;
		this.targetTypes = propertyTypes;
	}

	public IConverter<?> getConverter() {
		return converter;
	}

	public boolean match(Object queryObj, String queryPropertyName, Class<?> queryTargetType) {
		if (forClass != null && !forClass.isAssignableFrom(queryObj.getClass())) {
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

}