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
import com.swtxml.parser.IControllerObjectProvider;
import com.swtxml.parser.TagLibraryXmlParser;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.swt.processors.BuildWidgets;
import com.swtxml.swt.processors.SetAttributes;

public class SwtTagLibraryParser extends TagLibraryXmlParser implements IControllerObjectProvider {

	private Object controller;

	public SwtTagLibraryParser(Composite parent, Object controller) {
		super(getSwtNamespaceResolver(), new BuildWidgets(parent), new SetAttributes());
		this.controller = controller;
	}

	private static INamespaceResolver getSwtNamespaceResolver() {
		NamespaceResolver resolver = new NamespaceResolver();
		resolver.addNamespace("swt", new SwtNamespace());
		return resolver;
	}

	public void parse() {
		super.parse(controller.getClass(), "swtxml");
		super.injectById(controller);
	}

	public Object getController() {
		return controller;
	}

}