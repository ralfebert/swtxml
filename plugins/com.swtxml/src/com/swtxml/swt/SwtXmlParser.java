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

import org.eclipse.swt.widgets.Composite;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.definition.impl.NamespaceResolver;
import com.swtxml.swt.byid.ByIdInjector;
import com.swtxml.swt.processors.BuildWidgets;
import com.swtxml.swt.processors.CollectIds;
import com.swtxml.swt.processors.SetAttributes;
import com.swtxml.swt.processors.TagContextProcessor;
import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.tinydom.TinyDomParser;
import com.swtxml.util.context.Context;

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
		resolver.addNamespace("swt", SwtInfo.NAMESPACE);
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

		new ByIdInjector().inject(controller, ids);
	}

}