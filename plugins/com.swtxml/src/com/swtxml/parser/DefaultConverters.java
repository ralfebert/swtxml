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
package com.swtxml.parser;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.internal.runtime.ResourceTranslator;

import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;

public class DefaultConverters implements IAttributeConverter {

	public Object convert(TagInformation node, TagAttribute attr, Class<?> destClass) {

		String value = attr.getValue();
		if (destClass == Integer.TYPE) {
			return Integer.parseInt(value);
		}
		if (destClass == Boolean.TYPE) {
			return Boolean.parseBoolean(value);
		}
		if (destClass == Float.TYPE) {
			return Float.parseFloat(value);
		}
		if (destClass == Character.TYPE) {
			return value.charAt(0);
		}
		if (destClass == String.class) {
			// TODO: this should be separated out and configured
			// TODO: check speed issues (resource bundles seem to be cached
			// internally)
			// TODO: cleaner class loading
			if (value.startsWith("%") && attr.getParser() instanceof IControllerObjectProvider) {
				Object controller = ((IControllerObjectProvider) attr.getParser()).getController();
				if (controller != null) {
					try {
						return ResourceTranslator.getResourceString(null, value, ResourceBundle
								.getBundle("plugin", Locale.getDefault(), controller.getClass()
										.getClassLoader()));
					} catch (MissingResourceException e) {
						return "Missing message: " + value + " in "
								+ controller.getClass().getSimpleName() + " / plugin / "
								+ Locale.getDefault();
					}
				}
			}
			return value;
		}

		return NOT_CONVERTABLE;
	}

}
