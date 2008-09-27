package com.swtxml.swt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.parser.INamespaceResolver;
import com.swtxml.swt.metadata.SwtNamespace;

@Deprecated
public class CompatibilityNamespaceResolver implements INamespaceResolver {

	private static final SwtNamespace SWT_NAMESPACE = new SwtNamespace();
	private static Map<String, FakeTagDefinition> prototypeTags = new HashMap<String, FakeTagDefinition>();

	static {
		prototypeTags.put("list", new FakeTagDefinition("list"));
		prototypeTags.put("table", new FakeTagDefinition("table"));
		prototypeTags.put("row", new FakeTagDefinition("row"));
	}

	public INamespaceDefinition resolveNamespace(String uri) {
		if (uri.equals("class://com.swtxml.swt.SwtWidgetTagLibrary")) {
			return SWT_NAMESPACE;
		} else if (uri.equals("class://com.swtxml.swt.PrototypeTagLibrary")) {
			return new INamespaceDefinition() {

				public ITagDefinition getTag(String name) {
					return prototypeTags.get(name);
				}

				public Set<String> getTagNames() {
					return prototypeTags.keySet();
				}
			};
		}
		return null;
	}
}
