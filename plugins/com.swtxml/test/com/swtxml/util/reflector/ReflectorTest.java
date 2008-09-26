package com.swtxml.util.reflector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.ReflectorBean;

public class ReflectorTest {

	private final static Predicate<Method> getMethodNamePredicate(final String name) {
		return new Predicate<Method>() {

			public boolean apply(Method m) {
				return m.getName().equals(name);
			}

		};
	}

	private final static Predicate<IReflectorProperty> getReflectorPropertyNamePredicate(
			final String name) {
		return new Predicate<IReflectorProperty>() {

			public boolean apply(IReflectorProperty prop) {
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
		assertTrue(Iterables.find(testVoSetters, getMethodNamePredicate("setText")) != null);
		assertTrue("superclass setter", Iterables.find(testVoSetters,
				getMethodNamePredicate("setBaseText")) != null);
	}

	@Test(expected = NoSuchElementException.class)
	public void testFindPublicSettersContainNoProtectedSetters() {
		Iterables.find(testVoSetters, getMethodNamePredicate("setProtectedProperty"));
	}

	@Test
	public void testFindPublicSettersContainNoMultiArgumentSetMethods() {
		assertTrue(Collections2.filter(testVoSetters, getMethodNamePredicate("setMulti")).isEmpty());
	}

	@Test
	public void findPublicProperties() {
		Collection<IReflectorProperty> properties = Reflector.findPublicProperties(TestVO.class);
		assertTrue(Iterables.find(properties, getReflectorPropertyNamePredicate("text")) != null);
		assertTrue("superclass property", Iterables.find(properties,
				getReflectorPropertyNamePredicate("baseText")) != null);
		assertTrue("public fields not included", Collections2.filter(properties,
				getReflectorPropertyNamePredicate("publicText")).isEmpty());
		assertTrue("protected property not included", Collections2.filter(properties,
				getReflectorPropertyNamePredicate("publicText")).isEmpty());
		assertTrue("base protected field not included", Collections2.filter(properties,
				getReflectorPropertyNamePredicate("protectedProperty")).isEmpty());
	}

	@Test
	public void findPublicPropertiesIncludingPublicFields() {
		Collection<IReflectorProperty> properties = Reflector.findPublicProperties(TestVO.class,
				true);
		assertTrue(Iterables.find(properties, getReflectorPropertyNamePredicate("text")) != null);
		assertTrue("superclass property", Iterables.find(properties,
				getReflectorPropertyNamePredicate("baseText")) != null);
		assertTrue("public field", Collections2.filter(properties,
				getReflectorPropertyNamePredicate("publicText")) != null);
		assertTrue("base public field", Collections2.filter(properties,
				getReflectorPropertyNamePredicate("basePublicText")) != null);
		assertTrue("protected field not included", Collections2.filter(properties,
				getReflectorPropertyNamePredicate("protectedText")).isEmpty());
	}

	@Test
	public void testPropertyGetSet() {
		TestVO test = new TestVO();
		ReflectorBean bean = new ReflectorBean(TestVO.class, true);
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
}