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

import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.tag.Tag;

public class TagLibraryXmlParser {

	private INamespaceResolver namespaceResolver;

	public TagLibraryXmlParser(INamespaceResolver namespaceResolver) {
		super();
		this.namespaceResolver = namespaceResolver;
	}

	protected Tag parse(Class<?> clazz) {
		return parse(clazz, "xml");
	}

	protected Tag parse(Class<?> clazz, String extension) {
		String fname = clazz.getSimpleName() + "." + extension;
		InputStream resource = clazz.getResourceAsStream(fname);
		if (resource == null) {
			throw new XmlParsingException(fname + " not found in package "
					+ clazz.getPackage().getName());
		}

		return parse(fname, resource);
	}

	public <T> Tag parse(String filename, InputStream inputStream) {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		SAXParser parser = createSaxParser(parserFactory);

		TagLibrarySaxHandler saxHandler = new TagLibrarySaxHandler(namespaceResolver, filename);
		try {
			parser.parse(inputStream, saxHandler);
		} catch (Exception e) {
			throw new XmlParsingException(saxHandler.getLocationInfo() + e.getMessage(), e);
		}

		return saxHandler.getRoot();
	}

	private SAXParser createSaxParser(SAXParserFactory parserFactory) {
		try {
			return parserFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new XmlParsingException(e);
		} catch (SAXException e) {
			throw new XmlParsingException(e);
		}
	}

}