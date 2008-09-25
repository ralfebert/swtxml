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
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Control;

import com.swtxml.magic.Attribute;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.parser.IAttributeConverter.NotConvertable;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;

public class SwtHelper {

	public static enum SWTAlign {
		TOP(SWT.TOP), BOTTOM(SWT.BOTTOM), LEFT(SWT.LEFT), RIGHT(SWT.RIGHT), CENTER(SWT.CENTER), DEFAULT(
				SWT.DEFAULT);

		private int swtConstant;

		private SWTAlign(int swtConstant) {
			this.swtConstant = swtConstant;
		}

		public int getSwtConstant() {
			return swtConstant;
		}

	};

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

	// TODO: parser - abstract "get by id" out
	public static FormAttachment parseFormAttachment(TagInformation node, TagAttribute attr) {
		FormAttachment attachment = new FormAttachment();
		List<String> parts = new ArrayList<String>();
		int start = 0;
		String value = attr.getValue();
		for (int i = 0; i < value.length(); i++) {
			char charAt = value.charAt(i);
			if (charAt == '-' || charAt == '+') {
				parts.add(value.substring(start, i).trim());
				start = i;
			}
		}
		parts.add(value.substring(start, value.length()).trim());

		for (String part : parts) {
			if (part.endsWith("%")) {
				if (part.startsWith("+")) {
					part = part.substring(1);
				}
				attachment.numerator = Integer.parseInt(part.substring(0, part.length() - 1));
			} else {
				try {
					if (part.startsWith("+")) {
						part = part.substring(1);
					}
					attachment.offset = Integer.parseInt(part);
				} catch (NumberFormatException e) {
					String[] controlString = StringUtils.split(part, ".");
					if (controlString.length >= 1) {
						String id = controlString[0].trim();
						Control control = attr.getParser().getById(id, Control.class);
						if (control == null) {
							throw new TagLibraryException(node, "Control with id " + id
									+ " not found");
						}
						attachment.control = control;
					}
					if (controlString.length >= 2) {
						String align = controlString[1].trim();
						attachment.alignment = requireEnumValue(node, align, SWTAlign.class)
								.getSwtConstant();
					}
				}
			}
		}
		return attachment;
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