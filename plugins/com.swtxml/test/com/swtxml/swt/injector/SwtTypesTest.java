package com.swtxml.swt.injector;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
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
		IType<Color> colorType = new ColorType();

		Color color = colorType.convert(null, "#010203");
		assertEquals(1, color.getRed());
		assertEquals(2, color.getGreen());
		assertEquals(3, color.getBlue());

		color = colorType.convert(null, "#FFabCD");
		assertEquals(255, color.getRed());
		assertEquals(171, color.getGreen());
		assertEquals(205, color.getBlue());

		color = colorType.convert(null, "black");
		assertEquals(0, color.getRed());
		assertEquals(0, color.getGreen());
		assertEquals(0, color.getBlue());
	}

	@Test
	public void testGridLayout() {
		LayoutType layoutType = new LayoutType(layoutInjector);
		GridLayout layout = (GridLayout) layoutType.convert(null,
				"layout:grid;numColumns:2;horizontalSpacing:10;verticalSpacing:11;");
		assertEquals(2, layout.numColumns);
		assertEquals(10, layout.horizontalSpacing);
		assertEquals(11, layout.verticalSpacing);
	}

	@Test
	public void testRowLayout() {
		LayoutType layoutType = new LayoutType(layoutInjector);
		RowLayout layout = (RowLayout) layoutType.convert(null,
				"layout:row;type:vertical;spacing:5;");
		assertEquals(SWT.VERTICAL, layout.type);
		assertEquals(5, layout.spacing);
	}

	@Test
	public void testLayoutCompletion() {
		LayoutType layoutType = new LayoutType(layoutInjector);
		assertEquals("layout:§;", layoutType.getProposals(new Match("la§")).get(0).toString());
		assertEquals("layout:§;", layoutType.getProposals(new Match("§")).get(0).toString());
		assertEquals("type:§;layout:row;", layoutType.getProposals(new Match("ty§;layout:row;"))
				.get(0).toString());
		assertEquals("layout:row;type:§;", layoutType.getProposals(new Match("layout:row;ty§"))
				.get(0).toString());
		assertEquals("type:vertical;center:§;layout:row;", layoutType.getProposals(
				new Match("type:vertical;c§;layout:row;")).get(0).toString());
		assertEquals("type:vertical;center:§;layout:row;", layoutType.getProposals(
				new Match("type:vertical;c§layout:row;")).get(0).toString());
		assertEquals("layout:row;center:§;", layoutType.getProposals(new Match("layout:row; c§"))
				.get(0).toString());
		assertEquals("layout:row;center:true;§", layoutType.getProposals(
				new Match("layout:row;center:  t§")).get(0).toString());
		assertTrue(layoutType.getProposals(new Match("§;layout:row;")).size() > 0);
		assertEquals("type:VERTICAL;§layout:row;", layoutType.getProposals(
				new Match("type:v§;layout:row;")).get(0).toString());
		assertEquals(2, layoutType.getProposals(new Match("type:§;layout:row;")).size());
	}

	@Test
	public void testLayoutCompletionDoesNotContainAlreadySetProperties() {
		LayoutType layoutType = new LayoutType(layoutInjector);
		List<Match> proposals = layoutType.getProposals(new Match("type:vertical;layout:row;§"));
		List<String> proposalTexts = Lists.transform(proposals, new Function<Match, String>() {
			public String apply(Match m) {
				return m.getText();
			}
		});
		assertTrue(proposalTexts.contains("marginBottom:"));
		assertFalse(proposalTexts.contains("type:"));
	}

	@Test
	public void testLayoutDataSetter() {
		GridData data = (GridData) new LayoutDataType(layoutInjector).createLayoutData(
				new GridLayout(), "widthHint:120");
		assertEquals(120, data.widthHint);
	}

	@Test
	public void testPointType() {
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
