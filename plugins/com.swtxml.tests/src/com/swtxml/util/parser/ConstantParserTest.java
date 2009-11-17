/********************************import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.junit.Before;
import org.junit.Test;
Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.util.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.junit.Before;
import org.junit.Test;

public class ConstantParserTest {

	public static class TestConstants {
		public static int BLA = 1 << 17;
		public static int YAH = 1 << 17;
	}

	private ConstantParser swtConstants;

	@Before
	public void setup() {
		swtConstants = new ConstantParser(SWT.class);
	}

	@Test
	public void testGetIntValue() {
		assertEquals(SWT.READ_ONLY | SWT.BORDER, swtConstants.getIntValue("READ_ONLY,BORDER"));
		assertEquals(SWT.READ_ONLY | SWT.BORDER, swtConstants.getIntValue("READ_ONLY|BORDER"));

		try {
			swtConstants.getIntValue("READ_ONLY|BLABLA");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("BLABLA"));
		}
	}

	@Test
	public void testFilter() {
		ConstantParser s = swtConstants.filter("BEGINNING|CENTER");
		assertEquals(SWT.BEGINNING | SWT.CENTER, s.getIntValue("BEGINNING|CENTER"));
	}

	@Test(expected = Exception.class)
	public void testInvalidFilter() {
		ConstantParser s = swtConstants.filter("BEGINNING|XXX");
		assertEquals(SWT.BEGINNING | SWT.CENTER, s.getIntValue("BEGINNING|CENTER"));
	}

	@Test
	public void testInvalidFiltered() {
		ConstantParser s = swtConstants.filter("BEGINNING|CENTER");
		try {
			s.getIntValue("BEGINNING|LEFT");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("LEFT") && e.getMessage().contains("BEGINNING"));
		}
	}

	@Test
	public void testConstantConverterConverter() {

		ConstantParser cc = new ConstantParser(SWT.class);
		assertEquals(SWT.READ_ONLY | SWT.BORDER, cc.getIntValue("READ_ONLY,BORDER"));

		ConstantParser cc2 = new ConstantParser(TestConstants.class, SWT.class);
		assertEquals(SWT.READ_ONLY | SWT.BORDER | TestConstants.BLA, cc2
				.getIntValue("BLA,READ_ONLY,BORDER"));

	}

}
