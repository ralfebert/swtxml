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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.definition.INamespaceResolver;
import com.swtxml.resources.IDocumentResource;
import com.swtxml.util.parser.ParseException;

public class TinyDomParser implements IAdaptable {

	private INamespaceResolver namespaceResolver;
	protected IDocumentResource document;

	public TinyDomParser(INamespaceResolver namespaceResolver, IDocumentResource document) {
		super();
		this.namespaceResolver = namespaceResolver;
		this.document = document;
	}

	@SuppressWarnings("unchecked")
	public <A> A getAdapter(Class<A> adapterClass) {
		if (IDocumentResource.class.isAssignableFrom(adapterClass)) {
			return (A) document;
		}
		return null;
	}

	public final <T> Tag parse() {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		SAXParser parser = createSaxParser(parserFactory);

		TinyDomSaxHandler saxHandler = new TinyDomSaxHandler(namespaceResolver, document
				.getDocumentName());
		try {
			parser.parse(document.getInputSource(), saxHandler);
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