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
package com.swtxml.swt.byid;

import java.lang.reflect.Field;
import java.util.Collection;

import org.eclipse.core.runtime.Assert;

import com.swtxml.adapter.IIdResolver;
import com.swtxml.util.reflector.Reflector;
import com.swtxml.util.reflector.ReflectorException;
import com.swtxml.util.reflector.Subclasses;
import com.swtxml.util.reflector.Visibility;

/**
 * ByIdInjector injects values in annotated Object fields.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class ByIdInjector {

	/**
	 * Inspects 'object' for fields annotated with the {@link ById} annotation.
	 * For all such fields it resolves the value by the field name using
	 * 'idResolver' and injects these values in the fields.
	 * 
	 * @throws ReflectorException
	 *             if no value could be resolved for a field or on Java
	 *             reflection errors.
	 */
	public void inject(Object object, IIdResolver idResolver) throws ReflectorException {
		Assert.isNotNull(object, "object");
		Assert.isNotNull(idResolver, "idResolver");

		Collection<Field> fields = Reflector.findFields(Visibility.PRIVATE, Subclasses.INCLUDE)
				.annotatedWith(ById.class).all(object.getClass());

		for (Field field : fields) {
			try {
				Object value = idResolver.getById(field.getName(), field.getType());
				if (value == null) {
					throw new ReflectorException("No element with id \"" + field.getName()
							+ "\" found for injecting @ById " + object.getClass() + "."
							+ field.getName());
				}
				boolean oldAccess = field.isAccessible();
				field.setAccessible(true);
				field.set(object, value);
				field.setAccessible(oldAccess);
			} catch (Exception e) {
				throw new ReflectorException(e);
			}
		}
	}

}