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
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.adapter.MockAdapter;
import com.swtxml.resources.IDocumentResource;
import com.swtxml.util.context.Context;

public class ResourceBundleLabelTranslatorTest {

	private static final String NAME = "SomeFile";
	private static final String NAME_SWTXML = NAME + ".swtxml";
	private static final String MESSAGES = "messages";

	private static final String BUNDLE_MESSAGES = "bundle:messages";
	private static final String BUNDLE_PLUGIN = "bundle:plugin";

	private IAnswer<InputStream> classResourceAnswer(final Class<?> clazz, final String name) {
		return new IAnswer<InputStream>() {

			public InputStream answer() throws Throwable {
				return clazz.getResourceAsStream(name);
			}

		};
	}

	@Before
	public void setup() {
		Context.clear();
	}

	@Test
	public void testTranslationPlausability() {

		final Class<?> clazz = ResourceBundleLabelTranslatorTest.class;

		IDocumentResource doc = EasyMock.createMock(IDocumentResource.class);

		EasyMock.expect(doc.getDocumentName()).andReturn(NAME_SWTXML).anyTimes();

		EasyMock.expect(doc.resolve(NAME + "_de.properties")).andAnswer(
				classResourceAnswer(clazz, NAME + "_de.properties")).anyTimes();
		EasyMock.expect(doc.resolve(NAME + "_en.properties")).andReturn(null).anyTimes();
		EasyMock.expect(doc.resolve(NAME + ".properties")).andAnswer(
				classResourceAnswer(clazz, NAME + ".properties")).anyTimes();

		EasyMock.expect(doc.resolve(MESSAGES + "_en.properties")).andReturn(null).anyTimes();
		EasyMock.expect(doc.resolve(MESSAGES + "_de.properties")).andReturn(null).anyTimes();
		EasyMock.expect(doc.resolve(MESSAGES + ".properties")).andReturn(null).anyTimes();

		EasyMock.expect(doc.resolve(BUNDLE_MESSAGES + "_en.properties")).andReturn(null).anyTimes();
		EasyMock.expect(doc.resolve(BUNDLE_MESSAGES + "_de.properties")).andReturn(null).anyTimes();
		EasyMock.expect(doc.resolve(BUNDLE_MESSAGES + ".properties")).andReturn(null).anyTimes();

		EasyMock.replay(doc);

		Context.addAdapter(new MockAdapter(doc));

		ResourceBundleLabelTranslator translator = new ResourceBundleLabelTranslator(doc,
				Locale.GERMAN);
		assertEquals("Hallo", translator.translate("hello"));
		assertEquals("123", translator.translate("only_in_default"));

		translator = new ResourceBundleLabelTranslator(doc, Locale.ENGLISH);
		assertEquals("Hello", translator.translate("hello"));
		assertEquals("123", translator.translate("only_in_default"));
	}

	@Test
	public void testTranslatationOrder() {
		IDocumentResource doc = EasyMock.createStrictMock(IDocumentResource.class);

		EasyMock.expect(doc.getDocumentName()).andReturn(NAME_SWTXML);

		EasyMock.expect(doc.resolve(NAME + "_en_US.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(NAME + "_en.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(NAME + ".properties")).andReturn(null);

		EasyMock.expect(doc.resolve(MESSAGES + "_en_US.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(MESSAGES + "_en.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(MESSAGES + ".properties")).andReturn(null);

		EasyMock.expect(doc.resolve(BUNDLE_MESSAGES + "_en_US.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(BUNDLE_MESSAGES + "_en.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(BUNDLE_MESSAGES + ".properties")).andReturn(null);

		EasyMock.expect(doc.resolve(BUNDLE_PLUGIN + "_en_US.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(BUNDLE_PLUGIN + "_en.properties")).andReturn(null);
		EasyMock.expect(doc.resolve(BUNDLE_PLUGIN + ".properties")).andReturn(null);

		EasyMock.replay(doc);

		Context.addAdapter(new MockAdapter(doc));

		ResourceBundleLabelTranslator translator = new ResourceBundleLabelTranslator(doc, Locale.US);

		assertEquals("??? xxx ???", translator.translate("xxx"));

		EasyMock.verify(doc);
	}
}
