/**
 * Copyright (c) 2007, Ralf Ebert
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * Neither the name of the project team nor the names of its contributors may be
 * used to endorse or promote products derived from this software without specific
 * prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.swtxml.swt;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;

import com.swtxml.parser.XmlParsingException;

public class ConstantConverter {

	private final Class<?>[] constantClasses;
	private static Map<Class<?>, Map<String, Integer>> constantMap = new HashMap<Class<?>, Map<String, Integer>>();

	public ConstantConverter(Class<?>... constantClasses) {
		this.constantClasses = constantClasses;

		for (Class<?> cl : constantClasses) {
			if (!constantMap.containsKey(cl)) {
				constantMap.put(cl, extractConstants(cl));
			}
		}
	}

	private Map<String, Integer> extractConstants(Class<?> cl) {
		Map<String, Integer> constants = new HashMap<String, Integer>();
		try {
			Field[] fields = cl.getFields();
			for (Field field : fields) {
				if (Modifier.isPublic(field.getModifiers())
						&& Modifier.isStatic(field.getModifiers())
						&& field.getType() == Integer.TYPE) {
					constants.put(field.getName(), field.getInt(SWT.class));
				}
			}
		} catch (Exception e) {
			throw new XmlParsingException(e);
		}
		return constants;
	}

	public int getIntValue(String value) {

		int style = SWT.NONE;
		if (value == null) {
			return style;
		}
		String[] values = StringUtils.split(value, ";,|");
		for (String v : values) {
			v = v.trim().toUpperCase();

			Integer constant = null;
			for (Class<?> constantClass : constantClasses) {
				constant = constantMap.get(constantClass).get(v);
				if (constant != null) {
					break;
				}
			}

			if (constant == null) {
				throw new XmlParsingException("Unknown style constant: " + v + " from classes "
						+ ArrayUtils.toString(constantClasses));
			}

			style |= constant;
		}
		return style;

	}

}