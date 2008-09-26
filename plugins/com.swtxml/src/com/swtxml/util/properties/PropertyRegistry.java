package com.swtxml.util.properties;

import java.util.LinkedHashMap;

import com.swtxml.util.reflector.ReflectorBean;
import com.swtxml.util.types.IType;

public class PropertyRegistry {

	private LinkedHashMap<PropertyMatcher, IType<?>> types = new LinkedHashMap<PropertyMatcher, IType<?>>();
	private boolean includePublicFields;

	public PropertyRegistry(boolean includePublicFields) {
		this.includePublicFields = includePublicFields;
	}

	public <T> void add(PropertyMatcher matcher, IType<T> type) {
		types.put(matcher, type);
	}

	public <A> ClassProperties<A> getProperties(Class<A> clazz) {
		return new ClassProperties<A>(new ReflectorBean(clazz, includePublicFields), types);
	}
}
