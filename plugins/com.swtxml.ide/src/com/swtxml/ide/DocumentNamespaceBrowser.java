package com.swtxml.ide;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.extensions.ExtensionsNamespaceResolver;

public class DocumentNamespaceBrowser {

	private Map<String, INamespaceDefinition> definitionByURI = new HashMap<String, INamespaceDefinition>();
	private Map<INamespaceDefinition, String> prefixByDefinition = new HashMap<INamespaceDefinition, String>();

	public DocumentNamespaceBrowser(Document doc) {
		ExtensionsNamespaceResolver resolver = new ExtensionsNamespaceResolver();
		NamedNodeMap attributes = doc.getDocumentElement().getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attr = attributes.item(i);
			String nodeName = attr.getNodeName();
			if (nodeName.startsWith("xmlns")) {
				String prefix = nodeName;
				if (prefix.startsWith("xmlns:")) {
					prefix = prefix.substring(6);
				} else {
					prefix = prefix.substring(5);
				}
				String uri = attr.getNodeValue();
				INamespaceDefinition namespaceDefinition = resolver.resolveNamespace(uri);
				if (namespaceDefinition != null) {
					definitionByURI.put(uri, namespaceDefinition);
					prefixByDefinition.put(namespaceDefinition, prefix);
				}
			}
		}
	}

	public INamespaceDefinition getByURI(String namespaceURI) {
		return definitionByURI.get(namespaceURI);
	}

	public Collection<INamespaceDefinition> getAllDefinitions() {
		return definitionByURI.values();
	}

	public String getPrefix(INamespaceDefinition namespace) {
		String prefix = prefixByDefinition.get(namespace);
		if (prefix == null) {
			return null;
		}
		return prefix.length() > 0 ? prefix + ":" : "";
	}
}
