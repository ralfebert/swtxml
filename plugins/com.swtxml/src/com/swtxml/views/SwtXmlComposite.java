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

import org.eclipse.swt.widgets.Composite;

import com.swtxml.swt.SwtXmlParser;

/**
 * Extend this class to implement SWT Composites with SWT/XML. It will parse the
 * co-located .swtxml file (same package, same name).
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public abstract class SwtXmlComposite extends Composite {

	public SwtXmlComposite(Composite parent, int style) {
		super(parent, style);

		SwtXmlParser parser = new SwtXmlParser(this, this);
		parser.parse();

		setupView();
	}

	/**
	 * Override setupView to implement custom logic which is to be executed
	 * after the widgets have been created.
	 */
	protected void setupView() {

	}

}