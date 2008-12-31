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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.swtxml.swt.SwtXmlParser;

/**
 * Extend this class to implement JFace Dialogs with SWT/XML. It will parse the
 * co-located .swtxml file (same package, same name).
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class SwtXmlDialog extends Dialog {

	public SwtXmlDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected final Control createDialogArea(final Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite xmlComposite = new Composite(composite, SWT.NONE);
		xmlComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		SwtXmlParser parser = new SwtXmlParser(xmlComposite, this);
		parser.parse();
		setupView();
		return composite;
	}

	/**
	 * Override setupView to implement custom logic which is to be executed
	 * after the widgets have been created.
	 */
	protected void setupView() {

	}

}