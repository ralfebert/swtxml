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
package com.swtxml.resources;

import java.io.InputStream;

import org.xml.sax.InputSource;

/**
 * DocumentResource which loads SWT/XML documents from the classpath.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class ClassResource implements IDocumentResource {

	private Class<?> clazz;
	private String filename;

	public ClassResource(Class<?> clazz, String filename) {
		this.clazz = clazz;
		this.filename = filename;
	}

	public static ClassResource coLocated(Class<?> clazz, String extension) {
		return new ClassResource(clazz, clazz.getSimpleName() + "." + extension);
	}

	public String getDocumentName() {
		return filename;
	}

	public InputSource getInputSource() {
		return new InputSource(resolve(filename));
	}

	public InputStream resolve(String path) {
		if (path.startsWith(IDocumentResource.SCHEME_BUNDLE)) {
			return clazz.getClassLoader().getResourceAsStream(
					"/" + path.substring(IDocumentResource.SCHEME_BUNDLE.length()));
		}
		return clazz.getResourceAsStream(path);
	}
}
