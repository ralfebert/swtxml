package com.swtxml.util.properties;

import java.util.LinkedHashMap;

import com.swtxml.util.reflector.ReflectorBean;

public class PropertyRegistry {

	private LinkedHashMap<PropertyMatcher, ISetter> setters = new LinkedHashMap<PropertyMatcher, ISetter>();
	private boolean includePublicFields;

	public PropertyRegistry(boolean includePublicFields) {
		this.includePublicFields = includePublicFields;
	}

	public <T> void add(PropertyMatcher matcher, IConverter<T> converter) {
		add(matcher, new ConvertingSetter(converter));
	}

	public void add(PropertyMatcher matcher, ISetter setter) {
		setters.put(matcher, setter);
	}

	public <A> ClassProperties<A> getProperties(Class<A> clazz) {
		return new ClassProperties<A>(new ReflectorBean(clazz, includePublicFields), setters);
	}
}
