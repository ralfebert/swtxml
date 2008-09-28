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

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class KeyValueParserTest {

	@Test
	public void testParse() throws Exception {
		Map<String, String> values = KeyValueParser.parse(" test:xyz;  abc:def ;");
		assertEquals("xyz", values.get("test"));
		assertEquals("def", values.get("abc"));
		assertEquals(2, values.size());
	}

}
