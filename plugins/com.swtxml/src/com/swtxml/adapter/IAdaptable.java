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
 * Classes implementing this interface can adapt themselves to a required class
 * or interface. Same as {@link org.eclipse.core.runtime.IAdaptable} with
 * generic types.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface IAdaptable {

	public <A> A adaptTo(Class<A> adapterClass);

}
