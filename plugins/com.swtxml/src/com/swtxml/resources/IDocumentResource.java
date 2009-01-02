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
 * IDocumentResource represents an input document which can be processed by
 * SWT/XML.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface IDocumentResource {

	/**
	 * Scheme to resolve resources relative to the root of the bundle in which
	 * this document is located.
	 */
	public static final String SCHEME_BUNDLE = "bundle:";

	/**
	 * Returns the name of this document. Should be the filename without path.
	 */
	public String getDocumentName();

	/**
	 * Returns a SAX InputSource for this document.
	 */
	public InputSource getInputSource();

	/**
	 * Resolves a resource specified in this document. path can be prefixed with
	 * a url-like scheme to specify where to look for resources. schemes
	 * declared in this interface should be supported. If no scheme is given, it
	 * should be resolved relative to the document. Should return null if the
	 * resource was not found.
	 */
	public InputStream resolve(String path);

}
