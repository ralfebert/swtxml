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
package com.swtxml.i18n;

public class GracefulBundleLabelTranslator implements ILabelTranslator {

	private ILabelTranslator[] delegates;

	public GracefulBundleLabelTranslator(ILabelTranslator... delegates) {
		super();
		this.delegates = delegates;
	}

	public String translate(String key) {
		for (ILabelTranslator delegate : delegates) {
			String value = delegate.translate(key);
			if (value != null) {
				return value;
			}
		}
		return "??? " + key + " ???";
	}
}
