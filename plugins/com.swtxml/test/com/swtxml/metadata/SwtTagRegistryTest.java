package com.swtxml.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.junit.Before;
import org.junit.Test;

public class SwtTagRegistryTest {

	private SwtTagRegistry swtTagRegistry;
	private ITag buttonTag;

	@Before
	public void setUp() throws Exception {
		swtTagRegistry = new SwtTagRegistry();
		buttonTag = swtTagRegistry.getTag("Button");
	}

	@Test
	public void testGetTagMetaData() {
		assertEquals("Button", buttonTag.getName());

		assertEquals(null, swtTagRegistry.getTag("wegewg"));
		assertEquals(null, buttonTag.getAttribute("erherhe"));

		ITagAttribute textAttribute = buttonTag.getAttribute("text");
		assertEquals("text", textAttribute.getName());

		assertTrue(buttonTag.getAttributes().contains(textAttribute));
	}

	@Test
	public void testBuilder() {
		Shell shell = new Shell();
		SwtWidgetBuilder builder = buttonTag.adaptTo(SwtWidgetBuilder.class);
		Widget btn = builder.build(shell, SWT.BORDER);
		assertTrue(btn instanceof Button);
		assertEquals(shell, ((Button) btn).getParent());
	}

}