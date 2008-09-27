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
import com.swtxml.parser.ITagProcessor;
import com.swtxml.parser.TagLibraryXmlParser;
import com.swtxml.parser.XmlParsingException;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.swt.processors.BuildWidgets;
import com.swtxml.swt.processors.SetAttributes;
import com.swtxml.swt.properties.IIdResolver;
import com.swtxml.tag.Document;
import com.swtxml.tag.Tag;
import com.swtxml.util.context.Context;

public class SwtXmlParser extends TagLibraryXmlParser {

	private Object controller;
	private Composite parent;

	public SwtXmlParser(Composite parent, Object controller) {
		super(createSwtNamespaceResolver());
		this.parent = parent;
		this.controller = controller;
	}

	private static INamespaceResolver createSwtNamespaceResolver() {
		NamespaceResolver resolver = new NamespaceResolver();
		resolver.addNamespace("swt", new SwtNamespace());
		return resolver;
	}

	public void parse() {
		final Document document = super.parse(controller.getClass(), "swtxml");

		ITagProcessor[] processors = new ITagProcessor[] { new BuildWidgets(parent),
				new SetAttributes() };

		for (final ITagProcessor processor : processors) {
			for (final Tag tag : document.getRoot().depthFirst()) {
				Context.runWith(new Runnable() {
					public void run() {
						Context.addAdapter(tag);
						Context.addAdapter(document);
						try {
							processor.process(tag);
						} catch (Exception e) {
							throw new XmlParsingException(tag.getLocationInfo() + e.getMessage(), e);
						}
					}
				});
			}
		}

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