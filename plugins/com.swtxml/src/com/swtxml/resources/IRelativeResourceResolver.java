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

/**
 * An IRelativeResourceResolver is responsible for resolving resources which are
 * referred in a IDocumentResource by a relative path
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface IRelativeResourceResolver {

	public InputStream resolve(String path);

}
