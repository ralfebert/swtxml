package com.swtxml.util.properties;

public interface IType<T> {
	T convert(Object obj, String value);
}