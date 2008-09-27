package com.swtxml.swt;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.parser.INamespaceResolver;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.util.parser.ParseException;

@Deprecated
public class CompatibilityNamespaceResolver implements INamespaceResolver {

	private static final SwtNamespace SWT_NAMESPACE = new SwtNamespace();
	private Map<String, ? extends ITagDefinition> prototypeTags = new HashMap<String, ITagDefinition>();

	public INamespaceDefinition resolveNamespace(String uri) {
		if (uri.equals("class://com.swtxml.swt.SwtWidgetTagLibrary")) {
			return SWT_NAMESPACE;
		} else if (uri.equals("class://com.swtxml.swt.PrototypeTagLibrary")) {
			return new INamespaceDefinition() {

				public Map<String, ? extends ITagDefinition> getTags() {
					return prototypeTags;
				}
			};
		}
		throw new ParseException("Unknown namespace: " + uri);
	}

}
