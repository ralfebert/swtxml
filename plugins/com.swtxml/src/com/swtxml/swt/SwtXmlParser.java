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
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.swt.processors.BuildWidgets;
import com.swtxml.swt.processors.CollectIds;
import com.swtxml.swt.processors.TagContextProcessor;
import com.swtxml.swt.processors.SetAttributes;
import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.tinydom.TinyDomParser;
import com.swtxml.util.context.Context;
import com.swtxml.util.parser.ParseException;

public class SwtXmlParser extends TinyDomParser {

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
		final Tag root = super.parse(controller.getClass(), "swtxml");

		final CollectIds ids = new CollectIds();
		final ITagProcessor buildWidgets = new TagContextProcessor(new BuildWidgets(parent));
		final ITagProcessor setAttributes = new TagContextProcessor(new SetAttributes());

		root.depthFirst(ids);

		Context.runWith(new Runnable() {
			public void run() {
				Context.addAdapter(ids);
				root.depthFirst(buildWidgets);
				root.depthFirst(setAttributes);
			}
		});

		injectById(ids);
	}

	public void injectById(IIdResolver ids) {
		// TODO: consider superclass methods
		for (Field field : controller.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ById.class)) {
				try {
					Object value = ids.getById(field.getName(), field.getType());
					if (value == null) {
						throw new ParseException("No element with id " + field.getName()
								+ " found for injecting @ById");
					}
					boolean oldAccess = field.isAccessible();
					field.setAccessible(true);
					field.set(controller, value);
					field.setAccessible(oldAccess);
				} catch (Exception e) {
					throw new ParseException(e);
				}
			}
		}
	}

}