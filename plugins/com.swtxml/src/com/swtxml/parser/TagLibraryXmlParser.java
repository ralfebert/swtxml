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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.tag.Document;
import com.swtxml.tag.TagInformation;
import com.swtxml.util.context.Context;

public class TagLibraryXmlParser {

	private Document document;
	private ITagProcessor[] processors;
	private INamespaceResolver namespaceResolver;

	public TagLibraryXmlParser(INamespaceResolver namespaceResolver, ITagProcessor... processors) {
		super();
		this.namespaceResolver = namespaceResolver;
		this.processors = processors;
	}

	protected Document parse(Class<?> clazz) {
		return parse(clazz, "xml");
	}

	protected Document parse(Class<?> clazz, String extension) {
		String fname = clazz.getSimpleName() + "." + extension;
		InputStream resource = clazz.getResourceAsStream(fname);
		if (resource == null) {
			throw new XmlParsingException(fname + " not found in package "
					+ clazz.getPackage().getName());
		}

		return parse(fname, resource);
	}

	public <T> Document parse(String filename, InputStream inputStream) {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		try {
			SAXParser parser = parserFactory.newSAXParser();
			// TODO: don't keep document
			this.document = new Document();

			TagLibrarySaxHandler s = new TagLibrarySaxHandler(document, namespaceResolver, filename);
			parser.parse(inputStream, s);

			for (ITagProcessor processor : processors) {
				for (TagInformation node : document.getAllNodes()) {
					Context.addAdapter(node);
					Context.addAdapter(document);
					processor.process(node);
					Context.clear();
				}
			}
			return document;
		} catch (ParserConfigurationException e) {
			throw new XmlParsingException(e);
		} catch (SAXException e) {
			throw new XmlParsingException(e);
		} catch (IOException e) {
			throw new XmlParsingException(e);
		}
	}

	public <T> T getById(String id, Class<T> clazz) {
		return document.getById(id, clazz);
	}

	public void injectById(Object obj) {
		// TODO: consider superclass methods
		for (Field field : obj.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ById.class)) {
				try {
					Object value = getById(field.getName(), field.getType());
					if (value == null) {
						throw new XmlParsingException("No element with id " + field.getName()
								+ " found for injecting @ById");
					}
					boolean oldAccess = field.isAccessible();
					field.setAccessible(true);
					field.set(obj, value);
					field.setAccessible(oldAccess);
				} catch (Exception e) {
					throw new XmlParsingException(e);
				}
			}
		}
	}

}