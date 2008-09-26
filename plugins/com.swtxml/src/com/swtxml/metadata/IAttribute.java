package com.swtxml.metadata;

public interface IAttribute extends Comparable<IAttribute> {

	String getName();

	<T> T adaptTo(Class<T> clazz);

}
