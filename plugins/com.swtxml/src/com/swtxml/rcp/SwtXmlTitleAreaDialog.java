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

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtxml.swt.SwtTagLibraryParser;

public class SwtXmlTitleAreaDialog extends TitleAreaDialog {

	public SwtXmlTitleAreaDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(final Composite parent) {

		// TitleAreaDialog is a bit picky about the Composite you add to the
		// provided parent. It needs
		// to have a GridData layout attachment and beware to change anything
		// about that! Safest and
		// most convenient way seems to place a second Composite for free layout
		// choice
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite xmlComposite = new Composite(composite, SWT.NONE);
		xmlComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		SwtTagLibraryParser parser = new SwtTagLibraryParser(xmlComposite, this);
		parser.parse();
		return composite;

		// Layout Debug-by-color snippet
		// composite.setBackground(Display.getDefault().getSystemColor(SWT.
		// COLOR_BLUE));
		// xmlComposite.setBackground(Display.getDefault().getSystemColor(SWT.
		// COLOR_RED));

	}

}