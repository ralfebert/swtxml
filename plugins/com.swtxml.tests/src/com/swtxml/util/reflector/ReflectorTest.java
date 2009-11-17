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
package com.swtxml.util.reflector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.swtxml.swt.byid.ById;
import com.swtxml.swt.byid.ByIdView;
import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IFilter;

public class ReflectorTest {

	private final static IFilter<Method> getMethodNameFilter(final String name) {
		return new IFilter<Method>() {

			public boolean match(Method m) {
				return m.getName().equals(name);
			}

		};
	}

	private final static IFilter<IReflectorProperty> getReflectorPropertyNameFilter(
			final String name) {
		return new IFilter<IReflectorProperty>() {

			public boolean match(IReflectorProperty prop) {
				return prop.getName().equals(name);
			}

		};
	}

	private Collection<Method> testVoSetters;

	@Before
	public void setup() {
		testVoSetters = Reflector.findPublicSetters(TestVO.class);
	}

	@Test
	public void testFindPublicSetters() {
		assertTrue(CollectionUtils.find(testVoSetters, getMethodNameFilter("setText")) != null);
		assertTrue("superclass setter", CollectionUtils.find(testVoSetters,
				getMethodNameFilter("setBaseText")) != null);
	}

	public void testFindPublicSettersContainNoProtectedSetters() {
		assertNull(CollectionUtils.find(testVoSetters, getMethodNameFilter("setProtectedProperty")));
	}

	@Test
	public void testFindPublicSettersContainNoMultiArgumentSetMethods() {
		assertTrue(CollectionUtils.select(testVoSetters, getMethodNameFilter("setMulti")).isEmpty());
	}

	@Test
	public void findPublicProperties() {
		Collection<IReflectorProperty> properties = Reflector.findPublicProperties(TestVO.class,
				PublicFields.NONE);
		assertTrue(CollectionUtils.find(properties, getReflectorPropertyNameFilter("text")) != null);
		assertTrue("superclass property", CollectionUtils.find(properties,
				getReflectorPropertyNameFilter("baseText")) != null);
		assertTrue("public fields not included", CollectionUtils.select(properties,
				getReflectorPropertyNameFilter("publicText")).isEmpty());
		assertTrue("protected property not included", CollectionUtils.select(properties,
				getReflectorPropertyNameFilter("publicText")).isEmpty());
		assertTrue("base protected field not included", CollectionUtils.select(properties,
				getReflectorPropertyNameFilter("protectedProperty")).isEmpty());
	}

	@Test
	public void findPublicPropertiesIncludingPublicFields() {
		Collection<IReflectorProperty> properties = Reflector.findPublicProperties(TestVO.class,
				PublicFields.INCLUDE);
		assertTrue(CollectionUtils.find(properties, getReflectorPropertyNameFilter("text")) != null);
		assertTrue("superclass property", CollectionUtils.find(properties,
				getReflectorPropertyNameFilter("baseText")) != null);
		assertTrue("public field", CollectionUtils.select(properties,
				getReflectorPropertyNameFilter("publicText")) != null);
		assertTrue("base public field", CollectionUtils.select(properties,
				getReflectorPropertyNameFilter("basePublicText")) != null);
		assertTrue("protected field not included", CollectionUtils.select(properties,
				getReflectorPropertyNameFilter("protectedText")).isEmpty());
		assertTrue("static constants are not found", CollectionUtils.select(properties,
				getReflectorPropertyNameFilter("SOME_CONSTANT")).isEmpty());
	}

	@Test
	public void testPropertyGetSet() {
		TestVO test = new TestVO();
		ReflectorBean bean = new ReflectorBean(TestVO.class, PublicFields.INCLUDE);
		IReflectorProperty text = bean.getProperty("text");
		IReflectorProperty counter = bean.getProperty("counter");
		IReflectorProperty basePublicText = bean.getProperty("basePublicText");

		assertEquals(String.class, text.getType());
		assertEquals(Integer.TYPE, counter.getType());
		assertEquals(String.class, basePublicText.getType());

		assertTrue(text != null);
		assertTrue(counter != null);
		assertTrue(basePublicText != null);

		assertEquals(null, text.get(test));
		assertEquals(0, counter.get(test));
		assertEquals(null, basePublicText.get(test));

		text.set(test, "123");
		counter.set(test, 5);
		basePublicText.set(test, "456");

		assertEquals("123", text.get(test));
		assertEquals(5, counter.get(test));
		assertEquals("456", basePublicText.get(test));
	}

	@Test
	public void testFieldQuery() {
		assertEquals(
				"[private java.lang.String com.swtxml.swt.byid.ByIdView.test, private java.lang.String com.swtxml.swt.byid.ByIdView.otherField, private java.lang.Integer com.swtxml.swt.byid.ByIdBaseView.baseNumber, private java.lang.Integer com.swtxml.swt.byid.ByIdBaseView.baseOtherField]",
				Reflector.findFields(Visibility.PRIVATE, Subclasses.INCLUDE).all(ByIdView.class)
						.toString());
		assertEquals(
				"[private java.lang.String com.swtxml.swt.byid.ByIdView.test, private java.lang.Integer com.swtxml.swt.byid.ByIdBaseView.baseNumber]",
				Reflector.findFields(Visibility.PRIVATE, Subclasses.INCLUDE).annotatedWith(
						ById.class).all(ByIdView.class).toString());
	}

	@Test
	public void testMethodQuery() {
		assertEquals(
				"public void com.swtxml.util.reflector.TestVO.doSomethingMore(java.lang.String,int)",
				Reflector.findMethods(Visibility.PRIVATE, Subclasses.INCLUDE).name(
						"doSomethingMore").parameters(String.class, Integer.TYPE).exactOne(
						TestVO.class).toGenericString());
	}

	@Test
	public void testMethodQueryPublicQueryDoesntSeeProtected() {
		Collection<Method> methods = Reflector.findMethods(Visibility.PUBLIC, Subclasses.INCLUDE)
				.name("doSomething").parameters(String.class).all(TestVO.class);
		assertTrue(methods.toString(), methods.isEmpty());
	}

	@Test
	public void testMethodQueryOverwrittenMethodsSeenCorrectly() {
		MethodQuery query = Reflector.findMethods(Visibility.PRIVATE, Subclasses.INCLUDE).name(
				"doSomething").parameters(String.class);
		assertEquals(
				"protected void com.swtxml.util.reflector.TestVO.doSomething(java.lang.String)",
				query.exactOne(TestVO.class).toGenericString());
		assertEquals(
				"protected void com.swtxml.util.reflector.BaseTestVO.doSomething(java.lang.String)",
				query.exactOne(BaseTestVO.class).toGenericString());
	}
}
