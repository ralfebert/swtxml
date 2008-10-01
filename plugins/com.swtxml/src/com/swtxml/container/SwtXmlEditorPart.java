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
package com.swtxml.container;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.EditorPart;

import com.swtxml.swt.SwtXmlParser;

public abstract class SwtXmlEditorPart extends EditorPart {

	public SwtXmlEditorPart() {

	}

	@Override
	public final void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		Composite composite = new Composite(parent, SWT.NONE);
		SwtXmlParser parser = new SwtXmlParser(composite, this);
		parser.parse();

		setupView();
	}

	protected void setupView() {

	}

	@Override
	public void setFocus() {

	}

	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
