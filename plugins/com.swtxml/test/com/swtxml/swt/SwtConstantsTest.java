package com.swtxml.swt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.junit.Test;

public class SwtConstantsTest {

	@Test
	public void testGetIntValue() {
		SwtConstants constants = SwtConstants.SWT;
		assertEquals(SWT.READ_ONLY | SWT.BORDER, constants.getIntValue("READ_ONLY,BORDER"));
		assertEquals(SWT.READ_ONLY | SWT.BORDER, constants.getIntValue("READ_ONLY|BORDER"));

		try {
			constants.getIntValue("READ_ONLY|BLABLA");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("BLABLA"));
		}
	}

	@Test
	public void testRestricted() {
		SwtConstants s = SwtConstants.SWT.restricted("BEGINNING|CENTER");
		assertEquals(SWT.BEGINNING | SWT.CENTER, s.getIntValue("BEGINNING|CENTER"));
	}

	@Test(expected = Exception.class)
	public void testInvalidRestrictedStyle() {
		SwtConstants s = SwtConstants.SWT.restricted("BEGINNING|XXX");
		assertEquals(SWT.BEGINNING | SWT.CENTER, s.getIntValue("BEGINNING|CENTER"));
	}

	@Test
	public void testInvalidRestrictionStyle() {
		SwtConstants s = SwtConstants.SWT.restricted("BEGINNING|CENTER");
		try {
			s.getIntValue("BEGINNING|LEFT");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("LEFT") && e.getMessage().contains("BEGINNING"));
		}
	}
}
