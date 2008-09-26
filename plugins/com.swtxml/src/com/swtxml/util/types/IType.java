package com.swtxml.util.types;

public interface IType<T> {

	T convert(Object obj, String value);

}