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
package com.swtxml.adapter;

/**
 * IIdResolver implementations resolve objects by their id and adapt them to a
 * required class.
 * 
 * @author Ralf Ebert <ebert@ralfebert.de>
 */
public interface IIdResolver {

	public <T> T getById(String id, Class<T> clazz);

}
