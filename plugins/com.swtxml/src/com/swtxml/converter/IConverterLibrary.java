package com.swtxml.converter;

public interface IConverterLibrary {

	public <T> IConverter<T> forProperty(String name, Class<T> clazz);

}