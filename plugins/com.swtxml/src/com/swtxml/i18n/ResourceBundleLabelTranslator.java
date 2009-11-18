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
import org.eclipse.core.runtime.Assert;

import com.swtxml.resources.IDocumentResource;
import com.swtxml.util.lang.FilenameUtils;

/**
 * ResourceBundleLabelTranslator translates label keys for the given document
 * and locale.
 * 
 * It looks up resource bundles which are co-located with the document first
 * (same name, same package). So if you have a document "SomeFile.swtxml", it
 * looks for "SomeFile_[locale].properties". Then it looks for a file
 * "messages_[locale].properties" in the same package as the document. Then it
 * looks for "messages_[locale].properties" and "plugin_[locale].properties" in
 * the root of the bundle from which the document was loaded.
 * 
 * Locales are resolved in the same way as Java resolves ResourceBundles, i.e.
 * for locale "en_US" it looks for: "messages_en_US.properties", then
 * "messages_en.properties", then "messages.properties"
 * 
 * Should be created only when needed, as it loads and caches all relevant
 * resource bundle contents immediately.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class ResourceBundleLabelTranslator implements ILabelTranslator {

	private final List<ResourceBundle> resourceBundles;

	public ResourceBundleLabelTranslator(IDocumentResource document, Locale locale) {
		Assert.isNotNull(document, "document");

		String documentName = FilenameUtils.getBaseName(document.getDocumentName());
		List<String> resourceBundleNames = getResourceBundleNames(documentName, locale);
		resourceBundleNames.addAll(getResourceBundleNames("messages", locale));
		resourceBundleNames.addAll(getResourceBundleNames("bundle:messages", locale));
		resourceBundleNames.addAll(getResourceBundleNames("bundle:plugin", locale));

		this.resourceBundles = new ArrayList<ResourceBundle>();
		for (String name : resourceBundleNames) {
			try {
				InputStream resource = document.resolve(name + ".properties");
				if (resource != null) {
					resourceBundles.add(new PropertyResourceBundle(resource));
				}
			} catch (MissingResourceException e) {
				// ignore missing resources
			} catch (IOException e) {
				// ignore invalid resource bundles
			}
		}

	}

	public String translate(String key) {
		for (ResourceBundle resourceBundle : resourceBundles) {
			try {
				String value = resourceBundle.getString(key);
				if (value != null) {
					return value;
				}
			} catch (MissingResourceException e) {
				// ignore missing resources
			}
		}

		return "??? " + key + " ???";
	}

	private List<String> getResourceBundleNames(String baseName, Locale locale) {
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
