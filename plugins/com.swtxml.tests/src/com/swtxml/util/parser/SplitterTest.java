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
package com.swtxml.util.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SplitterTest {

	@Test
	public void testSplitter() {
		Splitter s = new Splitter(".,");
		assertArrayEquals(new String[] { "1", "2", "3", "4" }, s.split("1.2,3.4"));
		assertEquals("1.2.3.4", s.join(s.split("1.2,3.4")));
		assertEquals(true, s.isSeparator('.'));
		assertEquals(true, s.isSeparator(','));
		assertEquals(false, s.isSeparator('x'));
	}

}
