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

import com.swtxml.magic.MagicTagNodeObjectProxy;
import com.swtxml.parser.IControllerObjectProvider;
import com.swtxml.parser.IRootNodeAware;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.parser.TagLibraryXmlParser;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class SwtTagLibraryParser extends TagLibraryXmlParser implements IRootNodeAware,
		IControllerObjectProvider {

	private Composite parent;
	private Object controller;

	public SwtTagLibraryParser(Composite parent, Object controller) {
		super();
		this.parent = parent;
		this.controller = controller;
	}

	public void parse() {
		super.parse(controller.getClass(), "swtxml", parent.getClass());
		super.injectById(controller);
	}

	public Object getController() {
		return controller;
	}

	public TagNode rootTag(TagInformation tagInformation) {
		if (!tagInformation.getTagName().equals(Composite.class.getSimpleName())) {
			throw new TagLibraryException(tagInformation, "Invalid root tag "
					+ tagInformation.getTagName() + ", expected <"
					+ Composite.class.getSimpleName() + ">");
		}

		return new MagicTagNodeObjectProxy(tagInformation, parent);
	}

	@Override
	protected ClassLoader getClassLoader() {
		return (controller != null) ? controller.getClass().getClassLoader() : super
				.getClassLoader();
	}

}