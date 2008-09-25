package com.swtxml.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.eclipse.swt.widgets.Button;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

public class ReflectorTest {

	private final static Predicate<Method> getMethodNamePredicate(final String name) {
		return new Predicate<Method>() {

			public boolean apply(Method m) {
				return m.getName().equals(name);
			}

		};
	}

	private final static Predicate<ReflectorProperty> getReflectorPropertyNamePredicate(
			final String name) {
		return new Predicate<ReflectorProperty>() {

			public boolean apply(ReflectorProperty prop) {
				return prop.getName().equals(name);
			}

		};
	}

	private Collection<Method> buttonMethods;
	private Collection<ReflectorProperty> buttonProperties;

	@Before
	public void setup() {
		buttonMethods = Reflector.findPublicSetters(Button.class);
		buttonProperties = Reflector.findPublicProperties(Button.class);
	}

	@Test
	public void testFindPublicSetters() {
		assertTrue(Iterables.find(buttonMethods, getMethodNamePredicate("setText")) != null);
		assertTrue("superclass setter", Iterables.find(buttonMethods,
				getMethodNamePredicate("setSize")) != null);
	}

	@Test(expected = NoSuchElementException.class)
	public void testFindPublicSettersContainNoProtectedSetters() {
		Iterables.find(buttonMethods, getMethodNamePredicate("setRadioSelection"));
	}

	@Test
	public void testFindPublicSettersContainNoMultiArgumentSetMethods() {
		assertEquals(1, Collections2.filter(buttonMethods, getMethodNamePredicate("setBounds"))
				.size());
	}

	@Test
	public void findPublicProperties() {
		assertTrue(Iterables.find(buttonProperties, getReflectorPropertyNamePredicate("text")) != null);
		assertTrue("superclass property", Iterables.find(buttonProperties,
				getReflectorPropertyNamePredicate("size")) != null);
	}
}
