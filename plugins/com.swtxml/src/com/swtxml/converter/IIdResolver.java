package com.swtxml.converter;

public interface IIdResolver {

	public <T> T getById(String id, Class<T> clazz);

}
