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
package com.swtxml.rcp;

import org.eclipse.swt.widgets.Composite;

import com.swtxml.swt.SwtXmlParser;

public abstract class SwtXmlComposite extends Composite {

	public SwtXmlComposite(Composite parent, int style) {
		super(parent, style);

		SwtXmlParser parser = new SwtXmlParser(this, this);
		parser.parse();
	}

}