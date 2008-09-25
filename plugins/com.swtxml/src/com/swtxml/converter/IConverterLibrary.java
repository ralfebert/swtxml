package com.swtxml.converter;

public interface IConverterLibrary {

	public <T> IConverter<T> forProperty(Object obj, String name, Class<T> clazz);

}