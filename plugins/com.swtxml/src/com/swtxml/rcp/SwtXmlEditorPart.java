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
package com.swtxml.rcp;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.EditorPart;

import com.swtxml.swt.SwtTagLibraryParser;

public abstract class SwtXmlEditorPart extends EditorPart {

	public SwtXmlEditorPart() {

	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		Composite composite = new Composite(parent, SWT.NONE);
		SwtTagLibraryParser parser = new SwtTagLibraryParser(composite, this);
		parser.parse();

	}

	@Override
	public void setFocus() {

	}

}
