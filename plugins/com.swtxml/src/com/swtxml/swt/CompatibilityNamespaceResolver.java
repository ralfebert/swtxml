package com.swtxml.swt;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.parser.INamespaceResolver;
import com.swtxml.swt.metadata.SwtNamespace;

@Deprecated
public class CompatibilityNamespaceResolver implements INamespaceResolver {

	private static final SwtNamespace SWT_NAMESPACE = new SwtNamespace();

	public INamespaceDefinition resolveNamespace(String uri) {
		if (uri.equals("class://com.swtxml.swt.SwtWidgetTagLibrary")) {
			return SWT_NAMESPACE;
		}
		return null;
	}
}
