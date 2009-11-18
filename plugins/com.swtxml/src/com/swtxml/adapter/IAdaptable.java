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
 * com.swtxml.adapter.IAdaptable is identical to
 * {@link org.eclipse.core.runtime.IAdaptable} except that it has a generic type
 * signature.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface IAdaptable {

	public <T> T getAdapter(Class<T> adapterClass);

}