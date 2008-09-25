package com.swtxml.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Test;

public class SwtConverterTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testColor() {
		IConverter<Color> colorConverter = SwtConverters.to(Color.class);

		Color color = colorConverter.convert("#010203");
		assertEquals(1, color.getRed());
		assertEquals(2, color.getGreen());
		assertEquals(3, color.getBlue());

		color = colorConverter.convert("black");
		assertEquals(0, color.getRed());
		assertEquals(0, color.getGreen());
		assertEquals(0, color.getBlue());
	}

	@Test
	public void testSimpleTypes() {
		assertEquals(123, SwtConverters.to(Integer.class).convert("123"));
		assertEquals(123, SwtConverters.to(Integer.TYPE).convert("123"));
		assertEquals(true, SwtConverters.to(Boolean.class).convert("true"));
		assertEquals(true, SwtConverters.to(Boolean.TYPE).convert("true"));
		assertEquals(5.3f, SwtConverters.to(Float.TYPE).convert("5.3"));
		assertEquals(5.3f, SwtConverters.to(Float.TYPE).convert("5.3"));
		assertEquals('a', SwtConverters.to(Character.TYPE).convert("a"));
		assertEquals('a', SwtConverters.to(Character.TYPE).convert("a"));
	}

	@Test
	public void testStyle() {
		IConverter<?> converter = SwtConverters.forProperty("style", Integer.TYPE);
		assertEquals(SWT.READ_ONLY | SWT.BORDER, converter.convert("READ_ONLY,BORDER"));
		assertEquals(SWT.READ_ONLY | SWT.BORDER, converter.convert("READ_ONLY|BORDER"));

		try {
			converter.convert("READ_ONLY|BLABLA");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("BLABLA"));
		}

	}
}
