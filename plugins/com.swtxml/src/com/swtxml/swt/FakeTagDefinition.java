package com.swtxml.swt;

import java.util.Collections;
import java.util.Set;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;

public class FakeTagDefinition implements ITagDefinition {

	private String name;

	public FakeTagDefinition(String name) {
		super();
		this.name = name;
	}

	public IAttributeDefinition getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> getAttributeNames() {
		return Collections.emptySet();
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public int compareTo(ITagDefinition o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
