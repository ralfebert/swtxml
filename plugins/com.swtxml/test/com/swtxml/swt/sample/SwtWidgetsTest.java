package com.swtxml.swt.sample;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.swt.SwtXmlParser;

public class SwtWidgetsTest {

	private static final String SAMPLE_SWTXML = "SwtWidgetsExamplesWindow.swtxml";
	private SwtWidgetsExamplesWindow window;

	@Before
	public void setupShell() {
		window = new SwtWidgetsExamplesWindow(null);
		window.create();
	}

	@Test
	public void testWidgetHierarchy() {
		assertWindowHierarchy((Composite) window.getShell().getChildren()[0]);
	}

	@Test
	public void testParseFromStream() {
		Shell parent = new Shell();
		SwtXmlParser parser = new SwtXmlParser(parent, null);
		parser.parse(SAMPLE_SWTXML, getClass().getResourceAsStream(SAMPLE_SWTXML));
		assertWindowHierarchy(parent);
	}

	@Test
	public void testParseByColocatedXml() {
		Shell parent = new Shell();
		SwtXmlParser parser = new SwtXmlParser(parent, null);
		parser.parse(SwtWidgetsExamplesWindow.class, "swtxml");
		assertWindowHierarchy(parent);
	}

	private void assertWindowHierarchy(Composite rootComposite) {
		TabFolder tabFolder = (TabFolder) rootComposite.getChildren()[0];
		TabItem tabItem1 = tabFolder.getItems()[0];
		assertEquals("RowLayout", tabItem1.getText());
		Composite tabItem1Composite = (Composite) tabItem1.getControl();
		RowLayout tabItem1CompositeLayout = (RowLayout) tabItem1Composite.getLayout();
		assertEquals(SWT.VERTICAL, tabItem1CompositeLayout.type);
		assertEquals(5, tabItem1CompositeLayout.spacing);
	}

	@Test
	public void testInjectionById() {
		assertEquals("Sauber", window.getTestButton().getText());
	}
}
