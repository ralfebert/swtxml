package com.swtxml.contracts;

public interface IIdResolver {

	public <T> T getById(String id, Class<T> clazz);

}
