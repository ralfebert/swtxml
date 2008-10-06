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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.swtxml.swt.SwtXmlParser;

/**
 * Extend this class to implement JFace WizardPages with SWT/XML. It will parse
 * the co-located .swtxml file (same package, same name).
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class SwtXmlWizardPage extends WizardPage {

	public SwtXmlWizardPage() {
		super("");
	}

	public SwtXmlWizardPage(String pageName) {
		super(pageName);
	}

	public final void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		SwtXmlParser parser = new SwtXmlParser(composite, this);
		parser.parse();
		setControl(composite);
		setupView();
	}

	/**
	 * Override setupView to implement custom logic which is to be executed
	 * after the widgets have been created.
	 */
	protected void setupView() {

	}
}
