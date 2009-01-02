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

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import com.swtxml.adapter.MockAdapter;
import com.swtxml.resources.ClassResource;
import com.swtxml.util.context.Context;

public class EclipsePluginLabelTranslatorTest {

	@Test
	public void testTranslateEclipsePlugin() {
		Context.addAdapter(new MockAdapter(new ClassResource(
				ResourceBundleLabelTranslatorTest.class, "SomeFile.swtxml")));

		ResourceBundleLabelTranslator translator = new ResourceBundleLabelTranslator(Locale.GERMAN);
		assertEquals("Hallo", translator.translate("hello"));
		assertEquals("Hallo", translator.translate("plugin_hello"));

		translator = new ResourceBundleLabelTranslator(Locale.ENGLISH);
		assertEquals("Hello", translator.translate("hello"));
		assertEquals("Hello", translator.translate("plugin_hello"));
		assertEquals("??? xxx ???", translator.translate("xxx"));
	}

}