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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ResourceBundleLabelTranslator implements ILabelTranslator {

	protected final Class<?> loadFrom;

	public ResourceBundleLabelTranslator(Class<?> loadFrom) {
		super();
		this.loadFrom = loadFrom;
	}

	public String translate(String key) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle(loadFrom.getName(), Locale
					.getDefault(), loadFrom.getClassLoader());
			if (bundle == null) {
				return null;
			}
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return null;
		}
	}
}
