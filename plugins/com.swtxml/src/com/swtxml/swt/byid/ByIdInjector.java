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

import com.swtxml.contracts.IIdResolver;
import com.swtxml.util.lang.ContractProof;
import com.swtxml.util.reflector.ReflectorException;

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
		ContractProof.notNull(object, "object");
		ContractProof.notNull(idResolver, "idResolver");
		// TODO: use reflector api for finding these methods
		Class<? extends Object> clazz = object.getClass();
		do {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.isAnnotationPresent(ById.class)) {
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
			clazz = clazz.getSuperclass();
		} while (clazz != null && !Object.class.equals(clazz));
	}

}
