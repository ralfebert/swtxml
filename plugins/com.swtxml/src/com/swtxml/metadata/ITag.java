package com.swtxml.metadata;

import java.util.Collection;

public interface ITag {

	String getName();

	ITagAttribute getAttribute(String name);

	Collection<ITagAttribute> getAttributes();

}
