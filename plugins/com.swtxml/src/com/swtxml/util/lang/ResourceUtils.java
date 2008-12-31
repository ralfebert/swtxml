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
package com.swtxml.util.lang;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class ResourceUtils {

	public static String toString(InputStream input) {
		try {
			StringWriter writer = new StringWriter();
			InputStreamReader in = new InputStreamReader(input);
			char[] buffer = new char[2048];
			int i = 0;
			while ((i = in.read(buffer)) > 0) {
				writer.write(buffer, 0, i);
			}
			input.close();
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeIOException(e.getMessage(), e);
		}
	}

	public static InputStream getClassResource(Class<?> clazz, String extension) {
		String fname = clazz.getSimpleName() + "." + extension;
		InputStream resource = clazz.getResourceAsStream(fname);
		if (resource == null) {
			throw new RuntimeIOException(fname + " not found in package "
					+ clazz.getPackage().getName());
		}
		return resource;
	}

}
