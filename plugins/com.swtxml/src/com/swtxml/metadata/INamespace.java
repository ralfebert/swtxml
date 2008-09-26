package com.swtxml.metadata;

import java.util.Map;

public interface INamespace {

	public abstract Map<String, ? extends ITag> getTags();

}