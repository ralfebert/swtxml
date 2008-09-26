/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.swt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;

import com.swtxml.magic.Attribute;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.parser.IAttributeConverter.NotConvertable;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;

public class SwtHelper {

	public static enum SWTTableAlign {
		LEFT(SWT.LEFT), RIGHT(SWT.RIGHT), CENTER(SWT.CENTER);

		private int swtConstant;

		private SWTTableAlign(int swtConstant) {
			this.swtConstant = swtConstant;
		}

		public int getSwtConstant() {
			return swtConstant;
		}

	};

	public static final void injectAttribute(TagInformation tag, Object destObject,
			TagAttribute attr, boolean requireAnnotations) {
		String setterName = "set" + StringUtils.capitalize(attr.getName());
		List<Method> methods = new ArrayList<Method>();
		for (Method method : destObject.getClass().getMethods()) {
			if ((!requireAnnotations || method.isAnnotationPresent(Attribute.class))
					&& method.getName().equals(setterName)) {
				methods.add(method);
			}
		}
		boolean processed = false;
		Map<Class<?>, String> triedConverterTypes = new HashMap<Class<?>, String>();
		for (Method method : methods) {
			if (method.getParameterTypes().length != 1) {
				continue;
			}
			Class<?> convertTo = method.getParameterTypes()[0];
			Object convertedValue = attr.getConvertedValue(tag, convertTo);

			if (convertedValue instanceof NotConvertable) {
				triedConverterTypes.put(convertTo, ((NotConvertable) convertedValue).getReason());
			} else {
				if (processed) {
					throw new TagLibraryException(tag, "Ambigous attribute setters: " + methods);
				}
				try {
					method.invoke(destObject, new Object[] { convertedValue });
				} catch (Exception e) {
					throw new TagLibraryException(tag, e.getMessage(), e);
				}
				processed = true;
			}

		}
		if (!processed && !requireAnnotations) {
			Field field = null;
			try {
				field = destObject.getClass().getField(attr.getName());
			} catch (SecurityException e) {
				throw new TagLibraryException(tag, e);
			} catch (NoSuchFieldException e) {
				// ignore
			}
			if (field != null && Modifier.isPublic(field.getModifiers())) {
				Object convertedValue = attr.getConvertedValue(tag, field.getType());
				if (convertedValue instanceof NotConvertable) {
					triedConverterTypes.put(field.getType(), ((NotConvertable) convertedValue)
							.getReason());
				} else {
					try {
						field.set(destObject, convertedValue);
						processed = true;
					} catch (SecurityException e) {
						throw new TagLibraryException(tag, e);
					} catch (IllegalArgumentException e) {
						throw new TagLibraryException(tag,
								"Converter returned inappropriate value for " + attr + ": "
										+ convertedValue.getClass() + " instead of "
										+ field.getType(), e);
					} catch (IllegalAccessException e) {
						throw new TagLibraryException(tag, e);
					}
				}
			}

		}

		if (!processed) {
			throw new TagLibraryException(tag, "No suitable setter " + setterName + " found on "
					+ destObject.getClass().getCanonicalName() + " " + triedConverterTypes);
		}

	}

	public static <T extends Enum> T requireEnumValue(TagInformation node, String value,
			Class<T> enumClass) {
		T[] enumValues = enumClass.getEnumConstants();
		for (T e : enumValues) {
			if (e.name().equals(value.toUpperCase())) {
				return e;
			}
		}
		throw new TagLibraryException(node, "Invalid value \"" + value + "\" allowed are: "
				+ ArrayUtils.toString(enumValues));
	}

}