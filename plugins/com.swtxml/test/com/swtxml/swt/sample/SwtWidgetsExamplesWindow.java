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
package com.swtxml.swt.sample;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.swtxml.rcp.SwtXmlWindow;

public class SwtWidgetsExamplesWindow extends SwtXmlWindow {

	public SwtWidgetsExamplesWindow(Shell parentShell) {
		super(parentShell);
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

}
