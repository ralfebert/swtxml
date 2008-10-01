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



public class SimpleAdapter implements IAdaptable {

	private Object obj;

	public SimpleAdapter(Object obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public <A> A adaptTo(Class<A> adapterClass) {
		if (adapterClass.isAssignableFrom(obj.getClass())) {
			return (A) obj;
		}
		return null;
	}

}
