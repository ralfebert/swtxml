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
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

public class ResourceBundleLabelTranslatorTest {

	@Test
	public void testTranslate() {
		ResourceBundleLabelTranslator translator = new ResourceBundleLabelTranslator(
				ResourceBundleLabelTranslatorTest.class);
		Locale.setDefault(Locale.GERMAN);
		assertEquals("Hallo", translator.translate("hello"));
		Locale.setDefault(Locale.ENGLISH);
		assertEquals("Hello", translator.translate("hello"));
		assertNull(translator.translate("xxx"));
	}
}
