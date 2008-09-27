package com.swtxml.swt;

public interface IIdResolver {

	public <T> T getById(String id, Class<T> clazz);

}
