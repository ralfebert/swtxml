package com.swtxml.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.util.BaseTestVO;
import com.swtxml.util.TestVO;

public class SwtConverterTest {

	private static final IConverterLibrary CONV = SwtConverterLibrary.getInstance();
	private Composite composite;

	@Before
	public void setUp() throws Exception {
		composite = new Composite(new Shell(), SWT.NONE);
	}

	@Test
	public void testPropertyMatcher() {
		PropertyMatcher m1 = new PropertyMatcher(new ColorConverter(), BaseTestVO.class, "test",
				Integer.class, Integer.TYPE);
		assertTrue(m1.match(new TestVO(), "test", Integer.class));
		assertTrue(m1.match(new TestVO(), "test", Integer.TYPE));
		assertFalse(m1.match("hai", "test", Integer.class));
		assertFalse(m1.match(new TestVO(), "test1", Integer.class));
		assertFalse(m1.match(new TestVO(), "test", String.class));
		PropertyMatcher m2 = new PropertyMatcher(new ColorConverter(), null, null);
		assertTrue(m2.match(new TestVO(), "test", String.class));
	}

	@Test
	public void testColor() {
		IConverter<Color> colorConverter = CONV.forProperty(composite, "foreground", Color.class);

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
		assertEquals(123, CONV.forProperty(composite, "xxx", Integer.class).convert("123"));
		assertEquals(123, CONV.forProperty(composite, "xxx", Integer.TYPE).convert("123"));
		assertEquals(true, CONV.forProperty(composite, "xxx", Boolean.class).convert("true"));
		assertEquals(true, CONV.forProperty(composite, "xxx", Boolean.TYPE).convert("true"));
		assertEquals(5.3f, CONV.forProperty(composite, "xxx", Float.TYPE).convert("5.3"));
		assertEquals(5.3f, CONV.forProperty(composite, "xxx", Float.TYPE).convert("5.3"));
		assertEquals('a', CONV.forProperty(composite, "xxx", Character.TYPE).convert("a"));
		assertEquals('a', CONV.forProperty(composite, "xxx", Character.TYPE).convert("a"));
	}

	@Test
	public void testStyle() {
		IConverter<?> converter = CONV.forProperty(composite, "style", Integer.TYPE);
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
		IConverter<Layout> layoutConverter = CONV.forProperty(composite, "layout", Layout.class);
		GridLayout layout = (GridLayout) layoutConverter
				.convert("layout:grid;numColumns:2;horizontalSpacing:10;verticalSpacing:11;");
		assertEquals(2, layout.numColumns);
		assertEquals(10, layout.horizontalSpacing);
		assertEquals(11, layout.verticalSpacing);
	}
}
