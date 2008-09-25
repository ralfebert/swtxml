package com.swtxml.metadata;

public interface ITagAttribute extends Comparable<ITagAttribute> {

	String getName();

	<T> T adaptTo(Class<T> clazz);

}
