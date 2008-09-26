package com.swtxml.swt.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.metadata.IAttribute;

public class SwtNamespaceTest {

	private SwtNamespace swtTagRegistry;
	private SwtTag buttonTag;

	@Before
	public void setUp() throws Exception {
		swtTagRegistry = new SwtNamespace();
		buttonTag = swtTagRegistry.getTags().get("Button");
	}

	@Test
	public void testGetTagMetaData() {
		assertEquals("Button", buttonTag.getName());

		assertTrue(swtTagRegistry.getTags().size() > 0);
		assertTrue(buttonTag.getAttributes().size() > 0);

		IAttribute textAttribute = buttonTag.getAttributes().get("text");
		assertEquals("text", textAttribute.getName());
	}

	@Test
	public void testBuilder() {
		Shell shell = new Shell();
		Button btn = fakeBuildButton(shell);
		assertTrue(btn instanceof Button);
		assertEquals(shell, btn.getParent());
	}

	private Button fakeBuildButton(Composite parent) {
		SwtWidgetBuilder builder = buttonTag.adaptTo(SwtWidgetBuilder.class);
		return (Button) builder.build(parent, SWT.BORDER);
	}

}
