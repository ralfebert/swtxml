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

import org.xml.sax.InputSource;

/**
 * IDocumentResource represents an input document which can be parsed by
 * SWT/XML. Needs to be able to provide a informal document name (for error
 * messages), SAX input source and needs to be able to resolve resource path
 * which are specified by relative paths in this document.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface IDocumentResource extends IRelativeResourceResolver {

	public String getDocumentInfoName();

	public InputSource getInputSource();
}
