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
package com.swtxml.swt.byid;

public class ByIdView extends ByIdBaseView {

	@ById
	private String test;

	@SuppressWarnings("unused")
	private String otherField;

	public String getTestX() {
		return test;
	}

}
