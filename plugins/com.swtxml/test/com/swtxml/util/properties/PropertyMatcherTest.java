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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.junit.Test;

import com.swtxml.util.reflector.BaseTestVO;
import com.swtxml.util.reflector.TestVO;

public class PropertyMatcherTest {

	@Test
	public void testPropertyMatcher() {
		PropertyMatcher m1 = new PropertyMatcher(BaseTestVO.class, "test", Integer.class,
				Integer.TYPE);
		assertTrue(m1.match(TestVO.class, "test", Integer.class));
		assertTrue(m1.match(TestVO.class, "test", Integer.TYPE));
		assertFalse(m1.match(String.class, "test", Integer.class));
		assertFalse(m1.match(TestVO.class, "test1", Integer.class));
		assertFalse(m1.match(TestVO.class, "test", String.class));
		PropertyMatcher m2 = new PropertyMatcher();
		assertTrue(m2.match(TestVO.class, "test", String.class));

		assertTrue(new PropertyMatcher(TestVO.class, PropertyMatcher.ALL_PROPERTIES).match(
				TestVO.class, "xxx", String.class));

		new PropertyMatcher(Composite.class, "layout", Layout.class).match(Composite.class,
				"layout", Layout.class);
	}
}
