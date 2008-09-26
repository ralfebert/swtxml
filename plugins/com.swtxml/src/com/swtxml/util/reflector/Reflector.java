package com.swtxml.util.reflector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

	public static Collection<IReflectorProperty> findPublicProperties(Class<?> cl) {
		return findPublicProperties(cl, false);
	}

	public static Collection<IReflectorProperty> findPublicProperties(Class<?> cl,
			boolean includePublicFields) {
		Collection<IReflectorProperty> properties = new ArrayList<IReflectorProperty>();
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

		if (includePublicFields) {
			List<Field> fields = Arrays.asList(cl.getFields());
			for (Field f : fields) {
				properties.add(new ReflectorField(f));
			}
		}

		return properties;
	}

}
