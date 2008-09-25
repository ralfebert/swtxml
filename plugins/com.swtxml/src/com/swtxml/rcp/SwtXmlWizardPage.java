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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.swtxml.swt.SwtTagLibraryParser;

public class SwtXmlWizardPage extends WizardPage {

	public SwtXmlWizardPage() {
		super("");
	}

	public SwtXmlWizardPage(String pageName) {
		super(pageName);
	}

	public void createControl(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		SwtTagLibraryParser parser = new SwtTagLibraryParser(composite, this);
		parser.parse();
		setControl(composite);
	}
}
