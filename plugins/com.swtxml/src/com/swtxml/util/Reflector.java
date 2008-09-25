package com.swtxml.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class Reflector {

	public static Collection<Method> findPublicSetters(Class<?> cl) {
		return Collections2.filter(Arrays.asList(cl.getMethods()), new Predicate<Method>() {

			public boolean apply(Method m) {
				return m.getName().startsWith("set") && m.getParameterTypes().length == 1;
			}

		});
	}

	public static Collection<ReflectorProperty> findPublicProperties(Class<?> cl) {
		Collection<ReflectorProperty> properties = new ArrayList<ReflectorProperty>();
		Collection<Method> setters = findPublicSetters(cl);
		for (final Method setter : setters) {
			Collection<Method> getters = Collections2.filter(Arrays.asList(cl.getMethods()),
					new Predicate<Method>() {

						public boolean apply(Method m) {
							return m.getName().equals("g" + setter.getName().substring(1))
									&& m.getParameterTypes().length == 0
									&& m.getReturnType() == setter.getParameterTypes()[0];
						}

					});
			if (getters.size() == 1) {
				properties.add(new ReflectorProperty(getters.iterator().next(), setter));
			}
		}
		return properties;
	}
}
