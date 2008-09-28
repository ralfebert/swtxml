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
package com.swtxml.util.properties;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import com.swtxml.util.types.SimpleTypes;

public class BaseConvertersTest {

	@Test
	public void testSimpleTypeConverters() {
		assertTrue(Arrays.equals(new int[] { 1, 2, 3 }, SimpleTypes.INT_ARRAY.convert("1,2,3")));
	}
}
