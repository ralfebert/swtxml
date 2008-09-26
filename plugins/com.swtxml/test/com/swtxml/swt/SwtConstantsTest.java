package com.swtxml.swt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.junit.Test;

public class SwtConstantsTest {

	public static class TestConstants {

		public static int BLA = 1 << 17;

	}

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
	public void testFilter() {
		SwtConstants s = SwtConstants.SWT.filter("BEGINNING|CENTER");
		assertEquals(SWT.BEGINNING | SWT.CENTER, s.getIntValue("BEGINNING|CENTER"));
	}

	@Test(expected = Exception.class)
	public void testInvalidFilter() {
		SwtConstants s = SwtConstants.SWT.filter("BEGINNING|XXX");
		assertEquals(SWT.BEGINNING | SWT.CENTER, s.getIntValue("BEGINNING|CENTER"));
	}

	@Test
	public void testInvalidFiltered() {
		SwtConstants s = SwtConstants.SWT.filter("BEGINNING|CENTER");
		try {
			s.getIntValue("BEGINNING|LEFT");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("LEFT") && e.getMessage().contains("BEGINNING"));
		}
	}

	@Test
	public void testConstantConverterConverter() {

		SwtConstants cc = new SwtConstants(SWT.class);
		assertEquals(SWT.READ_ONLY | SWT.BORDER, cc.getIntValue("READ_ONLY,BORDER"));

		SwtConstants cc2 = new SwtConstants(TestConstants.class, SWT.class);
		assertEquals(SWT.READ_ONLY | SWT.BORDER | TestConstants.BLA, cc2
				.getIntValue("BLA,READ_ONLY,BORDER"));

	}

}
