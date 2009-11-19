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
package com.swtxml.extensions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.metadata.SwtNamespace;

public class ExtensionsNamespaceResolverTest {

	@Test
	public void testResolveNamespace() {
		assertEquals(SwtInfo.NAMESPACE, new ExtensionsNamespaceResolver()
				.resolveNamespace(SwtNamespace.getNamespace().getUri()));
	}
}
