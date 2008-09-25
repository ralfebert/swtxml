package com.swtxml.metadata;

import java.util.Collection;

public interface ITag extends Comparable<ITag> {

	String getName();

	ITagAttribute getAttribute(String name);

	Collection<ITagAttribute> getAttributes();

	<T> T adaptTo(Class<T> clazz);
}
