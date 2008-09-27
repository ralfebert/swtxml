package com.swtxml.swt.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.definition.IAttributeDefinition;

public class SwtNamespaceTest {

	private SwtNamespace swtTagRegistry;
	private WidgetTag buttonTag;

	@Before
	public void setUp() throws Exception {
		swtTagRegistry = new SwtNamespace();
		buttonTag = swtTagRegistry.getTag("Button");
	}

	@Test
	public void testGetTagMetaData() {
		assertEquals("Button", buttonTag.getName());

		assertTrue(swtTagRegistry.getTagNames().size() > 0);
		assertTrue(buttonTag.getAttributeNames().size() > 0);

		IAttributeDefinition textAttribute = buttonTag.getAttribute("text");
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
		return (Button) new WidgetBuilder(buttonTag).build(parent, SWT.BORDER);
	}

}
