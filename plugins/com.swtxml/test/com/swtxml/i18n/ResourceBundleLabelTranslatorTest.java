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

import java.io.InputStream;
import java.util.Locale;

import org.easymock.EasyMock;
import org.junit.Test;

import com.swtxml.adapter.MockAdapter;
import com.swtxml.resources.IDocumentResource;
import com.swtxml.util.context.Context;

public class ResourceBundleLabelTranslatorTest {

	@Test
	public void testTranslate() {
		String name = "ResourceBundleLabelTranslatorTest";
		String de_name = name + "_de.properties";
		String en_name = name + "_en.properties";
		String default_name = name + ".properties";

		Class<?> clazz = ResourceBundleLabelTranslatorTest.class;
		InputStream de_resource = clazz.getResourceAsStream(de_name);
		InputStream default_resource = clazz.getResourceAsStream(default_name);

		IDocumentResource resolver = EasyMock.createMock(IDocumentResource.class);
		EasyMock.expect(resolver.getDocumentName()).andReturn(name + ".swtxml").anyTimes();
		EasyMock.expect(resolver.resolve(de_name)).andReturn(de_resource).anyTimes();
		EasyMock.expect(resolver.resolve(en_name)).andReturn(null).anyTimes();
		EasyMock.expect(resolver.resolve(default_name)).andReturn(default_resource).anyTimes();
		EasyMock.replay(resolver);

		Context.addAdapter(new MockAdapter(resolver));

		ResourceBundleLabelTranslator translator = new ResourceBundleLabelTranslator(clazz,
				Locale.GERMAN);
		assertEquals("Hallo", translator.translate("hello"));
		translator = new ResourceBundleLabelTranslator(clazz, Locale.ENGLISH);
		assertEquals("Hello", translator.translate("hello"));
		assertEquals("??? xxx ???", translator.translate("xxx"));
	}
}
