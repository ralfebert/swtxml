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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import com.swtxml.resources.IDocumentResource;
import com.swtxml.util.context.Context;
import com.swtxml.util.eclipse.EclipseEnvironment;
import com.swtxml.util.lang.FilenameUtils;
import com.swtxml.util.parser.ParseException;

public class ResourceBundleLabelTranslator implements ILabelTranslator {

	protected final Class<?> loadFrom;
	private Locale locale;

	public ResourceBundleLabelTranslator(Class<?> loadFrom, Locale locale) {
		super();
		this.loadFrom = loadFrom;
		this.locale = locale;
	}

	public String translate(String key) {
		String value = translateFromCoLocatedResourceBundles(key);
		if (value == null) {
			value = translateFromEclipseResourceBundles(key);
		}
		if (value == null) {
			return "??? " + key + " ???";
		}
		return value;
	}

	private String translateFromCoLocatedResourceBundles(String key) {
		IDocumentResource document = Context.adaptTo(IDocumentResource.class);
		if (document == null) {
			throw new ParseException("No resolver available to resolve bundle resources!");
		}

		List<String> names = getResourceBundleNames(FilenameUtils.getBaseName(document
				.getDocumentName()));

		names.addAll(getResourceBundleNames("plugin"));
		names.addAll(getResourceBundleNames("messages"));
		for (String name : names) {
			InputStream resource = document.resolve(name + ".properties");
			if (resource != null) {
				try {
					PropertyResourceBundle resourceBundle = new PropertyResourceBundle(resource);
					String value = resourceBundle.getString(key);
					if (value != null) {
						return value;
					}
				} catch (MissingResourceException e) {
					// ignore missing resources
				} catch (IOException e) {
					// ignore invalid resource bundles
				}
			}
		}

		return null;
	}

	private String translateFromEclipseResourceBundles(String key) {
		try {
			if (EclipseEnvironment.isAvailable() && loadFrom != null) {
				ResourceBundle bundle = ResourceBundle.getBundle("plugin", locale, loadFrom
						.getClassLoader());
				if (bundle != null) {
					return bundle.getString(key);
				}
			}
			return null;
		} catch (MissingResourceException e) {
			return null;
		}
	}

	private List<String> getResourceBundleNames(String baseName) {
		List<String> results = new ArrayList<String>(4);
		if (StringUtils.isNotEmpty(locale.getLanguage())) {
			if (StringUtils.isNotEmpty(locale.getCountry())) {
				if (StringUtils.isNotEmpty(locale.getVariant())) {
					results.add(baseName + "_" + locale.getLanguage() + "_" + locale.getCountry()
							+ "_" + locale.getVariant());
				}
				results.add(baseName + "_" + locale.getLanguage() + "_" + locale.getCountry());
			}
			results.add(baseName + "_" + locale.getLanguage());
		}
		results.add(baseName);

		return results;
	}
}
