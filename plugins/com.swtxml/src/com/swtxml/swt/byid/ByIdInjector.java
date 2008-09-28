package com.swtxml.swt.byid;

import java.lang.reflect.Field;

import com.swtxml.contracts.IIdResolver;
import com.swtxml.util.parser.ParseException;

public class ByIdInjector {

	public void inject(Object controller, IIdResolver ids) {
		// TODO: consider superclass methods
		for (Field field : controller.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(ById.class)) {
				try {
					Object value = ids.getById(field.getName(), field.getType());
					if (value == null) {
						throw new ParseException("No element with id " + field.getName()
								+ " found for injecting @ById");
					}
					boolean oldAccess = field.isAccessible();
					field.setAccessible(true);
					field.set(controller, value);
					field.setAccessible(oldAccess);
				} catch (Exception e) {
					throw new ParseException(e);
				}
			}
		}
	}

}
