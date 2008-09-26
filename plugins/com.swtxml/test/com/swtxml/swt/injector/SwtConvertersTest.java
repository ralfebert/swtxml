package com.swtxml.swt.injector;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.swt.SwtHandling;
import com.swtxml.swt.properties.IIdResolver;
import com.swtxml.swt.properties.setter.ColorConverter;
import com.swtxml.swt.properties.setter.LayoutConverter;
import com.swtxml.swt.properties.setter.LayoutDataSetter;
import com.swtxml.swt.properties.setter.PointConverter;
import com.swtxml.util.properties.IConverter;
import com.swtxml.util.properties.PropertyRegistry;

public class SwtConvertersTest {

	private PropertyRegistry layoutInjector;

	@Before
	public void setup() {
		IIdResolver idResolver = createMock(IIdResolver.class);
		layoutInjector = SwtHandling.createLayoutProperties(idResolver);
	}

	@Test
	public void testColor() {
		IConverter<Color> colorConverter = new ColorConverter();

		Color color = colorConverter.convert("#010203");
		assertEquals(1, color.getRed());
		assertEquals(2, color.getGreen());
		assertEquals(3, color.getBlue());

		color = colorConverter.convert("#FFabCD");
		assertEquals(255, color.getRed());
		assertEquals(171, color.getGreen());
		assertEquals(205, color.getBlue());

		color = colorConverter.convert("black");
		assertEquals(0, color.getRed());
		assertEquals(0, color.getGreen());
		assertEquals(0, color.getBlue());
	}

	@Test
	public void testGridLayout() {
		LayoutConverter layoutConverter = new LayoutConverter(layoutInjector);
		GridLayout layout = (GridLayout) layoutConverter
				.convert("layout:grid;numColumns:2;horizontalSpacing:10;verticalSpacing:11;");
		assertEquals(2, layout.numColumns);
		assertEquals(10, layout.horizontalSpacing);
		assertEquals(11, layout.verticalSpacing);
	}

	@Test
	public void testRowLayout() {
		LayoutConverter layoutConverter = new LayoutConverter(layoutInjector);
		RowLayout layout = (RowLayout) layoutConverter
				.convert("layout:row;type:vertical;spacing:5;");
		assertEquals(SWT.VERTICAL, layout.type);
		assertEquals(5, layout.spacing);

	}

	@Test
	public void testLayoutDataSetter() {
		GridData data = (GridData) new LayoutDataSetter(layoutInjector).createLayoutData(
				new GridLayout(), "widthHint:120");
		assertEquals(120, data.widthHint);
	}

	@Test
	public void testPointConverter() {
		assertEquals(new Point(12, 141), new PointConverter().convert("12x141"));
	}
}