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
package com.swtxml.util.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.swtxml.adapter.IAdaptable;

public class ContextTest {

	@Test
	public void testAdaptTo() {
		assertNull(Context.adaptTo(String.class));
		Context.addAdapter(new IAdaptable() {

			@SuppressWarnings("unchecked")
			public <T> T getAdapter(Class<T> adapter) {
				if (adapter.isAssignableFrom(String.class)) {
					return (T) "test";
				}
				return null;
			}

		});
		assertEquals("test", Context.adaptTo(String.class));
		Context.clear();
		assertNull(Context.adaptTo(String.class));
	}

}
