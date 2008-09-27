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
package com.swtxml.swt;

import java.lang.reflect.Field;

import org.eclipse.swt.widgets.Composite;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.definition.impl.NamespaceResolver;
import com.swtxml.parser.ById;
import com.swtxml.parser.TagLibraryXmlParser;
import com.swtxml.parser.XmlParsingException;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.swt.processors.BuildWidgets;
import com.swtxml.swt.processors.SetAttributes;
import com.swtxml.swt.properties.IIdResolver;
import com.swtxml.tag.Document;

public class SwtXmlParser extends TagLibraryXmlParser {

	private Object controller;

	public SwtXmlParser(Composite parent, Object controller) {
		super(createSwtNamespaceResolver(), new BuildWidgets(parent), new SetAttributes());
		this.controller = controller;
	}

	private static INamespaceResolver createSwtNamespaceResolver() {
		NamespaceResolver resolver = new NamespaceResolver();
		resolver.addNamespace("swt", new SwtNamespace());
		return resolver;
	}

	public void parse() {
		Document document = super.parse(controller.getClass(), "swtxml");
		injectById(document);
	}

	public void injectById(IIdResolver ids) {
		// TODO: consider superclass methods
		for (Field field : controller.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ById.class)) {
				try {
					Object value = ids.getById(field.getName(), field.getType());
					if (value == null) {
						throw new XmlParsingException("No element with id " + field.getName()
								+ " found for injecting @ById");
					}
					boolean oldAccess = field.isAccessible();
					field.setAccessible(true);
					field.set(controller, value);
					field.setAccessible(oldAccess);
				} catch (Exception e) {
					throw new XmlParsingException(e);
				}
			}
		}
	}

}