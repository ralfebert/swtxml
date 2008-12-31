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
package com.swtxml.swt.sample;

import java.util.Locale;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.swtxml.swt.byid.ById;
import com.swtxml.views.SwtXmlWindow;

public class SwtWidgetsExamplesWindow extends SwtXmlWindow {

	@ById
	private Button testButton;

	@ById
	private Label someImageLabel;

	public SwtWidgetsExamplesWindow(Shell parentShell) {
		super(parentShell);
		Locale.setDefault(Locale.ENGLISH);
	}

	public static void main(String[] args) {
		try {
			Display.getCurrent();
			SwtWidgetsExamplesWindow window = new SwtWidgetsExamplesWindow(null);
			window.setBlockOnOpen(true);
			window.open();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public Button getTestButton() {
		return testButton;
	}

	protected void coolButtonClicked() {
		System.err.println("Cool.");
	}

	public Label getSomeImageLabel() {
		return someImageLabel;
	}

}
