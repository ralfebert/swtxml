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

public class MockAdapter implements IAdaptable {

	private Object obj;

	public MockAdapter(Object obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> adapterClass) {
		if (adapterClass.isAssignableFrom(obj.getClass())) {
			return (T) obj;
		}
		return null;
	}

}
