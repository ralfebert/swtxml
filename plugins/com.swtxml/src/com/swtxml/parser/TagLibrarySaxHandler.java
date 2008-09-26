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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.swtxml.tag.Document;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class TagLibrarySaxHandler extends DefaultHandler {

	private final String xmlFilename;
	private Locator locator;
	private List<TagNode> allNodes = new ArrayList<TagNode>();
	private final TagLibraryXmlParser parser;
	private final Document document;

	public Document getDocument() {
		return document;
	}

	TagLibrarySaxHandler(TagLibraryXmlParser parser, String xmlFilename) {
		this.parser = parser;
		this.xmlFilename = xmlFilename;
		this.document = new Document();
	}

	private static final String CLASS_SCHEME = "class://";
	private Map<String, ITagLibrary> tagLibraries = new HashMap<String, ITagLibrary>();
	private Stack<TagNode> parsingStack = new Stack<TagNode>();

	private ITagLibrary getTagLibrary(String namespaceUri) {

		ITagLibrary tagLibrary = tagLibraries.get(namespaceUri);

		if (tagLibrary == null) {

			if (!namespaceUri.toLowerCase().startsWith(CLASS_SCHEME)) {
				throw new XmlParsingException(getLocationInfo() + "Invalid namespace uri \""
						+ namespaceUri + "\": (only " + CLASS_SCHEME + " is supported)\"");
			}

			String tagLibraryClassName = namespaceUri.substring(CLASS_SCHEME.length());
			try {
				Class<?> tagLibraryClass = parser.getClassLoader().loadClass(tagLibraryClassName);
				if (!ITagLibrary.class.isAssignableFrom(tagLibraryClass)) {
					throw new XmlParsingException(getLocationInfo() + tagLibraryClassName
							+ " must implement " + ITagLibrary.class.getCanonicalName());
				}
				tagLibrary = (ITagLibrary) tagLibraryClass.newInstance();
				tagLibraries.put(namespaceUri, tagLibrary);
			} catch (ClassNotFoundException e) {
				throw new XmlParsingException(getLocationInfo() + "Class " + tagLibraryClassName
						+ " for " + namespaceUri + " not found!", e);
			} catch (InstantiationException e) {
				throw new XmlParsingException(getLocationInfo() + e.getMessage(), e);
			} catch (IllegalAccessException e) {
				throw new XmlParsingException(getLocationInfo() + e.getMessage(), e);
			}
		}

		return tagLibrary;
	}

	@Override
	public void startElement(String namespaceUri, String localName, String qName,
			Attributes attributes) throws SAXException {
		List<TagAttribute> attributeList = new ArrayList<TagAttribute>();
		ITagLibrary tagLibrary = getTagLibrary(namespaceUri);
		TagInformation tagInformation = new TagInformation(document, tagLibrary, parsingStack
				.isEmpty() ? null : parsingStack.peek(), localName, getLocationInfo(), parsingStack
				.size(), attributeList);
		for (int i = 0; i < attributes.getLength(); i++) {
			String uri = attributes.getURI(i);
			if (StringUtils.isEmpty(uri) || uri.equals(namespaceUri)) {
				attributeList.add(new TagAttribute(parser, tagLibrary, attributes.getLocalName(i),
						attributes.getValue(i), true));

			} else {
				attributeList.add(new TagAttribute(parser, getTagLibrary(uri), attributes
						.getLocalName(i), attributes.getValue(i), false));
			}
		}

		TagNode tag = null;
		if (parser instanceof IRootNodeAware && parsingStack.isEmpty()) {
			tag = ((IRootNodeAware) parser).rootTag(tagInformation);
		} else {
			tag = tagLibrary.tag(tagInformation);
		}

		allNodes.add(tag);
		parsingStack.push(tag);
	}

	private String getLocationInfo() {
		return xmlFilename + " [line " + locator.getLineNumber() + "] ";
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		parsingStack.pop();
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public List<TagNode> getAllNodes() {
		return allNodes;
	}

}