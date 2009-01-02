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
 * IDocumentResource represents an input document which can be parsed by
 * SWT/XML. Needs to be able to provide a informal document name (for error
 * messages), SAX input source and needs to be able to resolve resource path
 * which are specified by relative paths in this document.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface IDocumentResource {

	public static final String SCHEME_BUNDLE = "bundle:";

	public String getDocumentName();

	public InputSource getInputSource();

	/**
	 * Resolves a resource relative to the document. path can be prefixed with a
	 * url-like scheme to specify where to look for resources. schemes declared
	 * in this interface should be supported. Should return null if the resource
	 * was not found.
	 */
	public InputStream resolve(String path);

}
