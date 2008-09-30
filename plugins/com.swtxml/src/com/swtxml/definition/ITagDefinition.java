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
package com.swtxml.definition;

import java.util.Set;

public interface ITagDefinition {

	public static final ITagDefinition ROOT = new RootTagDefinition();

	String getName();

	Set<String> getAttributeNames();

	public IAttributeDefinition getAttribute(String name);
}
