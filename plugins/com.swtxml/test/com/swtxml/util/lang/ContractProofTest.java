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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class ContractProofTest {

	@Test
	public void testNotNull() {
		ContractProof.notNull("test", "test");
		try {
			ContractProof.notNull(null, "test");
			fail("expected exception");
		} catch (ContractException e) {
			assertTrue(e.getMessage().contains("test"));
		}
	}

	@Test
	public void testSafePut() {
		Map<String, String> map = new HashMap<String, String>();

		ContractProof.safePut(map, "a", "AAA");
		ContractProof.safePut(map, "b", "BBB");
		assertEquals("AAA", map.get("a"));

		try {
			ContractProof.safePut(map, "a", "CCC");
			fail("expected exception");
		} catch (ContractException e) {
			assertTrue(e.getMessage().contains("AAA"));
			assertTrue(e.getMessage().contains("CCC"));
		}
	}

}
