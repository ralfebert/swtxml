package com.swtxml.converter;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
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
	public void testGridLayout() {
		LayoutConverter layoutConverter = new LayoutConverter();
		GridLayout layout = (GridLayout) layoutConverter
				.convert("layout:grid;numColumns:2;horizontalSpacing:10;verticalSpacing:11;");
		assertEquals(2, layout.numColumns);
		assertEquals(10, layout.horizontalSpacing);
		assertEquals(11, layout.verticalSpacing);
	}

	@Test
	public void testRowLayout() {
		LayoutConverter layoutConverter = new LayoutConverter();
		RowLayout layout = (RowLayout) layoutConverter
				.convert("layout:row;type:vertical;spacing:5;");
		assertEquals(SWT.VERTICAL, layout.type);
		assertEquals(5, layout.spacing);

	}

	@Test
	public void testLayoutDataSetter() {
		GridData data = (GridData) new LayoutDataSetter().createLayoutData(new GridLayout(),
				"widthHint:120");
		assertEquals(120, data.widthHint);
	}
}
