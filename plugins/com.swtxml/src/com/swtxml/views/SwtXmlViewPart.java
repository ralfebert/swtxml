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
package com.swtxml.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.swtxml.swt.SwtXmlParser;

/**
 * Extend this class to implement RCP ViewParts with SWT/XML. It will parse the
 * co-located .swtxml file (same package, same name).
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class SwtXmlViewPart extends ViewPart {

	public SwtXmlViewPart() {

	}

	@Override
	public final void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		Composite composite = new Composite(parent, SWT.NONE);
		SwtXmlParser parser = new SwtXmlParser(composite, this);
		parser.parse();

		setupView();
	}

	@Override
	public void setFocus() {

	}

	/**
	 * Override setupView to implement custom logic which is to be executed
	 * after the widgets have been created.
	 */
	protected void setupView() {

	}
}
