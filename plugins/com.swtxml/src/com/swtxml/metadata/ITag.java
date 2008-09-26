package com.swtxml.metadata;

import java.util.Map;

public interface ITag extends Comparable<ITag> {

	String getName();

	Map<String, ? extends IAttribute> getAttributes();

	<T> T adaptTo(Class<T> clazz);
}
