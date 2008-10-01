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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.swtxml.swt.SwtXmlParser;

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

	protected void setupView() {

	}
}
