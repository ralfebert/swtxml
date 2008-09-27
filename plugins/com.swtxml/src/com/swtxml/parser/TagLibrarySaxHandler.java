/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.tag.Document;
import com.swtxml.tag.TagInformation;
import com.swtxml.util.parser.ParseException;

public class TagLibrarySaxHandler extends DefaultHandler {

	private final String xmlFilename;
	private Locator locator;
	private final Document document;
	private final INamespaceResolver namespaceResolver;
	private final Map<String, INamespaceDefinition> namespaces = new HashMap<String, INamespaceDefinition>();

	TagLibrarySaxHandler(Document document, INamespaceResolver namespaceResolver, String xmlFilename) {
		this.document = document;
		this.namespaceResolver = namespaceResolver;
		this.xmlFilename = xmlFilename;
	}

	private Stack<TagInformation> parserStack = new Stack<TagInformation>();

	@Override
	public void startElement(String namespaceUri, String localName, String qName,
			Attributes attributes) throws SAXException {
		Map<String, String> attributeList = new HashMap<String, String>();
		INamespaceDefinition namespace = getNamespace(namespaceUri);
		ITagDefinition tagDefinition = namespace.getTag(localName);
		if (tagDefinition == null) {
			throw new ParseException("Unknown tag \"" + localName + "\" for namespace "
					+ namespaceUri);
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			String uri = attributes.getURI(i);
			if (StringUtils.isEmpty(uri) || uri.equals(namespaceUri)) {
				attributeList.put(attributes.getLocalName(i), attributes.getValue(i));
			} else {
				// TODO: reintroduce foreign attributes
				// attributeList.add(new TagAttribute(parser,
				// getTagLibrary(uri), attributes
				// .getLocalName(i), attributes.getValue(i), false));
			}
		}
		TagInformation tagInformation = new TagInformation(document, tagDefinition, parserStack
				.isEmpty() ? null : parserStack.peek(), localName, getLocationInfo(), parserStack
				.size(), attributeList);

		parserStack.push(tagInformation);
	}

	private INamespaceDefinition getNamespace(String namespaceUri) {
		INamespaceDefinition namespace = namespaces.get(namespaceUri);
		if (namespace == null) {
			namespace = namespaceResolver.resolveNamespace(namespaceUri);
			if (namespace == null) {
				throw new ParseException("Unknown namespace: " + namespaceUri);
			}
			namespaces.put(namespaceUri, namespace);
		}
		return namespace;
	}

	private String getLocationInfo() {
		return xmlFilename + " [line " + locator.getLineNumber() + "] ";
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		parserStack.pop();
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

}