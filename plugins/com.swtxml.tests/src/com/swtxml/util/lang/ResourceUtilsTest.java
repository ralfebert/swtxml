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
package com.swtxml.util.lang;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ResourceUtilsTest {

	@Test
	public void testToStringInputStream() {
		String text = ResourceUtils.toString(getClass().getResourceAsStream("sometext.txt"));
		assertEquals(StringUtils.repeat(StringUtils.repeat("10", 56) + "\n", 142).trim(), text);
	}

	@Test
	public void testGetClassResource() {
		assertEquals("hallo", ResourceUtils.toString(ResourceUtils.getClassResource(ResourceUtilsTest.class, "txt")));
	}
}
