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
import com.swtxml.swt.types.ColorType;
import com.swtxml.swt.types.LayoutDataType;
import com.swtxml.swt.types.LayoutType;
import com.swtxml.swt.types.PointType;
import com.swtxml.swt.types.StyleType;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IType;

public class SwtTypesTest {

	private PropertyRegistry layoutInjector;

	@Before
	public void setup() {
		IIdResolver idResolver = createMock(IIdResolver.class);
		layoutInjector = SwtHandling.createLayoutProperties(idResolver);
	}

	@Test
	public void testColor() {
		IType<Color> colorConverter = new ColorType();

		Color color = colorConverter.convert(null, "#010203");
		assertEquals(1, color.getRed());
		assertEquals(2, color.getGreen());
		assertEquals(3, color.getBlue());

		color = colorConverter.convert(null, "#FFabCD");
		assertEquals(255, color.getRed());
		assertEquals(171, color.getGreen());
		assertEquals(205, color.getBlue());

		color = colorConverter.convert(null, "black");
		assertEquals(0, color.getRed());
		assertEquals(0, color.getGreen());
		assertEquals(0, color.getBlue());
	}

	@Test
	public void testGridLayout() {
		LayoutType layoutConverter = new LayoutType(layoutInjector);
		GridLayout layout = (GridLayout) layoutConverter.convert(null,
				"layout:grid;numColumns:2;horizontalSpacing:10;verticalSpacing:11;");
		assertEquals(2, layout.numColumns);
		assertEquals(10, layout.horizontalSpacing);
		assertEquals(11, layout.verticalSpacing);
	}

	@Test
	public void testRowLayout() {
		LayoutType layoutConverter = new LayoutType(layoutInjector);
		RowLayout layout = (RowLayout) layoutConverter.convert(null,
				"layout:row;type:vertical;spacing:5;");
		assertEquals(SWT.VERTICAL, layout.type);
		assertEquals(5, layout.spacing);

	}

	@Test
	public void testLayoutDataSetter() {
		GridData data = (GridData) new LayoutDataType(layoutInjector).createLayoutData(
				new GridLayout(), "widthHint:120");
		assertEquals(120, data.widthHint);
	}

	@Test
	public void testPointConverter() {
		assertEquals(new Point(12, 141), new PointType().convert(null, "12x141"));
	}

	@Test
	public void testStyleType() {
		StyleType type = new StyleType(SwtHandling.SWT);
		assertEquals(SWT.BORDER | SWT.COLOR_RED, type.convert(null, "border|color_red"));
		assertEquals("border|BORDER_DASH§", type.getProposals(new Match("border|border_da§xxxx"))
				.get(0).toString());
	}
}
