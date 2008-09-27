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

import com.swtxml.parser.IControllerObjectProvider;
import com.swtxml.parser.TagLibraryXmlParser;
import com.swtxml.swt.processors.BuildWidgets;
import com.swtxml.swt.processors.SetAttributes;

public class SwtTagLibraryParser extends TagLibraryXmlParser implements IControllerObjectProvider {

	private Composite parent;
	private Object controller;

	public SwtTagLibraryParser(Composite parent, Object controller) {
		super(new CompatibilityNamespaceResolver(), new BuildWidgets(parent), new SetAttributes());
		this.parent = parent;
		this.controller = controller;
	}

	public void parse() {
		super.parse(controller.getClass(), "swtxml");
		super.injectById(controller);
	}

	public Object getController() {
		return controller;
	}

	@Override
	protected ClassLoader getClassLoader() {
		return (controller != null) ? controller.getClass().getClassLoader() : super
				.getClassLoader();
	}

}