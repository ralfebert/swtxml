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
import org.eclipse.swt.graphics.Color;

import com.swtxml.swt.ConstantConverter;
import com.swtxml.swt.SwtHelper;

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

	public void testConvertStringToStyle() {

		assertEquals(SWT.READ_ONLY | SWT.BORDER, SwtHelper.convertStringToStyle("READ_ONLY,BORDER"));
		assertEquals(SWT.READ_ONLY | SWT.BORDER, SwtHelper.convertStringToStyle("READ_ONLY|BORDER"));

		try {
			SwtHelper.convertStringToStyle("READ_ONLY|BLABLA");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("BLABLA"));
		}

	}

	public void testColors() {

		Color color = SwtHelper.getColor("#010203");
		assertEquals(1, color.getRed());
		assertEquals(2, color.getGreen());
		assertEquals(3, color.getBlue());

		color = SwtHelper.getColor("black");
		assertEquals(0, color.getRed());
		assertEquals(0, color.getGreen());
		assertEquals(0, color.getBlue());

	}

}