package com.swtxml.swt.injector;

public interface IIdResolver {

	public <T> T getById(String id, Class<T> clazz);

}
