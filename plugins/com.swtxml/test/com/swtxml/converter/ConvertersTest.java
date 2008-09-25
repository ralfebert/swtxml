package com.swtxml.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.junit.Test;

public class ConvertersTest {

	@Test
	public void testColor() {
		IConverter<Color> colorConverter = new ColorConverter();

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
	public void testStyle() {
		IConverter<?> converter = new StyleConverter();
		assertEquals(SWT.READ_ONLY | SWT.BORDER, converter.convert("READ_ONLY,BORDER"));
		assertEquals(SWT.READ_ONLY | SWT.BORDER, converter.convert("READ_ONLY|BORDER"));

		try {
			converter.convert("READ_ONLY|BLABLA");
			fail("expected exception");
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("BLABLA"));
		}

	}

	@Test
	public void testLayout() {
		LayoutConverter layoutConverter = new LayoutConverter();
		GridLayout layout = (GridLayout) layoutConverter
				.convert("layout:grid;numColumns:2;horizontalSpacing:10;verticalSpacing:11;");
		assertEquals(2, layout.numColumns);
		assertEquals(10, layout.horizontalSpacing);
		assertEquals(11, layout.verticalSpacing);
	}
}
