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
package com.swtxml.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.swtxml.util.eclipse.EclipseEnvironment;
import com.swtxml.util.lang.ResourceUtils;

public class ClassResourceTest {

	private ClassResource resource;

	@Before
	public void setup() {
		resource = new ClassResource(ClassResourceTest.class, "sometext.txt");
	}

	@Test
	public void testGetDocumentName() {
		assertEquals("sometext.txt", resource.getDocumentName());
	}

	@Test
	public void testGetInputSource() {
		assertEquals("hello", ResourceUtils.toString(resource.getInputSource().getByteStream()));
	}

	@Test
	public void testResolve() {
		assertEquals("hello", ResourceUtils.toString(resource.resolve("sometext.txt")));
		InputStream plugin_de = resource.resolve("bundle:plugin_de.properties");

		if (EclipseEnvironment.isAvailable()) {
			assertNotNull("plugin_de was not resolved from plugin path", plugin_de);
			assertTrue(ResourceUtils.toString(plugin_de).contains("plugin_hello"));
		} else {
			assertNull("plugin_de can only be resolved in plugin context - something is wrong",
					plugin_de);
		}
	}
}
