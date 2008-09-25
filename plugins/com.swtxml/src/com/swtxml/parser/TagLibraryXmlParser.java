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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.swtxml.tag.TagNode;

public class TagLibraryXmlParser {

	private Map<String, TagNode> nodesById;
	private TagNode rootNode;

	protected <T> T parse(Class<?> clazz, Class<T> rootNodeClass) {
		return parse(clazz, "xml", rootNodeClass);
	}

	protected <T> T parse(Class<?> clazz, String extension, Class<T> rootNodeClass) {
		String fname = clazz.getSimpleName() + "." + extension;
		InputStream resource = clazz.getResourceAsStream(fname);
		if (resource == null) {
			throw new XmlParsingException(fname + " not found in package "
					+ clazz.getPackage().getName());
		}

		return parse(fname, resource, rootNodeClass);
	}

	protected <T> T parse(String filename, InputStream inputStream, Class<T> rootNodeClass) {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		try {
			SAXParser parser = parserFactory.newSAXParser();
			TagLibrarySaxHandler saxHandler = new TagLibrarySaxHandler(this, filename);
			parser.parse(inputStream, saxHandler);

			List<TagNode> allNodes = saxHandler.getAllNodes();
			rootNode = allNodes.get(0);
			this.nodesById = saxHandler.getNodesById();

			for (TagNode node : allNodes) {
				// TODO: freeze children list after this
				TagNode parent = node.getParent();
				if (parent != null) {
					parent.getChildren().add(node);
				}
			}

			for (TagNode node : allNodes) {
				node.process();
			}

			return rootNode.get(rootNodeClass);
		} catch (ParserConfigurationException e) {
			throw new XmlParsingException(e);
		} catch (SAXException e) {
			throw new XmlParsingException(e);
		} catch (IOException e) {
			throw new XmlParsingException(e);
		}
	}

	private List<IAttributeConverter> converters = null;

	public final List<IAttributeConverter> getDefaultConverters() {
		if (converters == null) {
			converters = new ArrayList<IAttributeConverter>();
			configureConverters(converters);
		}
		return converters;
	}

	protected void configureConverters(List<IAttributeConverter> converters) {
		converters.add(new DefaultConverters());
	}

	public <T> T getById(String id, Class<T> clazz) {
		TagNode node = nodesById.get(id);
		if (node == null) {
			return null;
		}

		return node.get(clazz);
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

	protected ClassLoader getClassLoader() {
		return this.getClass().getClassLoader();
	}

	public TagNode getRootNode() {
		return rootNode;
	}

}