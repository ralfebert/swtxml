package com.swtxml.swt.injector;

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
import org.junit.After;
import org.junit.Test;

import com.swtxml.contracts.MockAdapter;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.types.ColorType;
import com.swtxml.swt.types.LayoutDataType;
import com.swtxml.swt.types.LayoutType;
import com.swtxml.swt.types.PointType;
import com.swtxml.swt.types.StyleType;
import com.swtxml.util.context.Context;
import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IFunction;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IType;

public class SwtTypesTest {

	@After
	public void clearContext() {
		Context.clear();
	}

	@Test
	public void testColor() {
		IType<Color> colorType = new ColorType();

		Color color = colorType.convert("#010203");
		assertEquals(1, color.getRed());
		assertEquals(2, color.getGreen());
		assertEquals(3, color.getBlue());

		color = colorType.convert("#FFabCD");
		assertEquals(255, color.getRed());
		assertEquals(171, color.getGreen());
		assertEquals(205, color.getBlue());

		color = colorType.convert("black");
		assertEquals(0, color.getRed());
		assertEquals(0, color.getGreen());
		assertEquals(0, color.getBlue());
	}

	@Test
	public void testGridLayout() {
		LayoutType layoutType = new LayoutType();
		GridLayout layout = (GridLayout) layoutType
				.convert("layout:grid;numColumns:2;horizontalSpacing:10;verticalSpacing:11;");
		assertEquals(2, layout.numColumns);
		assertEquals(10, layout.horizontalSpacing);
		assertEquals(11, layout.verticalSpacing);
	}

	@Test
	public void testRowLayout() {
		LayoutType layoutType = new LayoutType();
		RowLayout layout = (RowLayout) layoutType.convert("layout:row;type:vertical;spacing:5;");
		assertEquals(SWT.VERTICAL, layout.type);
		assertEquals(5, layout.spacing);
	}

	@Test
	public void testLayoutCompletion() {
		LayoutType layoutType = new LayoutType();
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
		LayoutType layoutType = new LayoutType();
		List<Match> proposals = layoutType.getProposals(new Match("type:vertical;layout:row;§"));
		List<String> proposalTexts = CollectionUtils.collect(proposals,
				new IFunction<Match, String>() {
					public String apply(Match m) {
						return m.getText();
					}
				});
		assertTrue(proposalTexts.contains("marginBottom:"));
		assertFalse(proposalTexts.contains("type:"));
	}

	@Test
	public void testLayoutDataSetter() {
		Context.addAdapter(new MockAdapter(new GridLayout()));
		LayoutDataType layoutDataType = new LayoutDataType();
		GridData data = (GridData) layoutDataType.convert("widthHint:120");
		assertEquals(120, data.widthHint);
	}

	@Test
	public void testLayoutDataContentAssist() {
		Context.addAdapter(new MockAdapter(new GridLayout()));
		LayoutDataType type = new LayoutDataType();
		assertEquals("widthHint:§;", type.getProposals(new Match("wi§")).get(0).toString());
		assertEquals("verticalAlignment:CENTER;§", type.getProposals(
				new Match("verticalAlignment:c§")).get(0).toString());

	}

	@Test
	public void testPointType() {
		assertEquals(new Point(12, 141), new PointType().convert("12x141"));
	}

	@Test
	public void testStyleType() {
		StyleType type = new StyleType(SwtInfo.SWT);
		assertEquals(SWT.BORDER | SWT.COLOR_RED, type.convert("border|color_red"));
		assertEquals("border|BORDER_DASH§", type.getProposals(new Match("border|border_da§xxxx"))
				.get(0).toString());
	}
}
