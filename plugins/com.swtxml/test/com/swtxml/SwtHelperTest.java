/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;

import com.swtxml.swt.ConstantConverter;

public class SwtHelperTest extends TestCase {

	public static class TestConstants {

		public static int BLA = 1 << 17;

	}

	public void testConstantConverterConverter() {

		ConstantConverter cc = new ConstantConverter(SWT.class);
		assertEquals(SWT.READ_ONLY | SWT.BORDER, cc.getIntValue("READ_ONLY,BORDER"));

		ConstantConverter cc2 = new ConstantConverter(TestConstants.class, SWT.class);
		assertEquals(SWT.READ_ONLY | SWT.BORDER | TestConstants.BLA, cc2
				.getIntValue("BLA,READ_ONLY,BORDER"));

	}

}