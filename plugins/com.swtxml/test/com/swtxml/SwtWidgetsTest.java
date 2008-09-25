package com.swtxml;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.junit.Before;
import org.junit.Test;

public class SwtWidgetsTest {

	private Shell shell;

	@Before
	public void setupShell() {
		SwtWidgetsExamplesWindow window = new SwtWidgetsExamplesWindow(null);
		window.create();
		shell = window.getShell();
	}

	@Test
	public void testWidgetHierarchy() {
		Composite composite = (Composite) shell.getChildren()[0];
		TabFolder tabFolder = (TabFolder) composite.getChildren()[0];
		TabItem tabItem1 = tabFolder.getItems()[0];
		assertEquals("RowLayout", tabItem1.getText());
		Composite tabItem1Composite = (Composite) tabItem1.getControl();
		RowLayout tabItem1CompositeLayout = (RowLayout) tabItem1Composite.getLayout();
		assertEquals(SWT.VERTICAL, tabItem1CompositeLayout.type);
		assertEquals(5, tabItem1CompositeLayout.spacing);
	}
}
