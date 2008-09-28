package com.swtxml.definition;

import java.util.Collections;
import java.util.Set;

class RootTagDefinition implements ITagDefinition {

	public IAttributeDefinition getAttribute(String name) {
		return null;
	}

	public Set<String> getAttributeNames() {
		return Collections.emptySet();
	}

	public String getName() {
		return "ROOT";
	}

	public boolean isAllowedIn(ITagDefinition tagDefinition) {
		return false;
	}

}
