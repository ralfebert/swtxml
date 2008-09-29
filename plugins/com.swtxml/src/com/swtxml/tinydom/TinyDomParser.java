/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.tinydom;

import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.util.lang.IOUtils;
import com.swtxml.util.parser.ParseException;

public class TinyDomParser {

	private INamespaceResolver namespaceResolver;

	public TinyDomParser(INamespaceResolver namespaceResolver) {
		super();
		this.namespaceResolver = namespaceResolver;
	}

	public final Tag parse(Class<?> clazz) {
		return parse(clazz, "xml");
	}

	public final Tag parse(Class<?> clazz, String extension) {
		return parse(clazz.getSimpleName() + "." + extension, IOUtils.getClassResource(clazz,
				extension));
	}

	public final <T> Tag parse(String filename, InputStream inputStream) {
		return parse(filename, new InputSource(inputStream));
	}

	public final <T> Tag parse(String filename, InputSource source) {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		SAXParser parser = createSaxParser(parserFactory);

		TinyDomSaxHandler saxHandler = new TinyDomSaxHandler(namespaceResolver, filename);
		try {
			parser.parse(source, saxHandler);
		} catch (Exception e) {
			throw new ParseException(saxHandler.getLocationInfo() + e.getMessage(), e);
		}

		onParseCompleted(saxHandler.getRoot());
		return saxHandler.getRoot();
	}

	protected void onParseCompleted(Tag root) {

	}

	private SAXParser createSaxParser(SAXParserFactory parserFactory) {
		try {
			return parserFactory.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new ParseException(e);
		} catch (SAXException e) {
			throw new ParseException(e);
		}
	}

}