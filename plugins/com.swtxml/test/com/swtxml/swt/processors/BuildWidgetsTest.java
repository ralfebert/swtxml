package com.swtxml.swt.processors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.tinydom.Tag;

public class BuildWidgetsTest {

	@Test
	public void testBuildWidgets() {

		Shell shell = new Shell();
		SwtNamespace swt = SwtInfo.NAMESPACE;

		Tag root = new Tag(swt.getTag("Composite"), null, "-", new HashMap<String, String>());
		Tag btn = new Tag(swt.getTag("Button"), root, "-", new HashMap<String, String>());

		BuildWidgets builder = new BuildWidgets(shell);
		root.depthFirst(builder);

		assertEquals(shell, root.adaptTo(Control.class));
		assertTrue(btn.adaptTo(Control.class) instanceof Button);
	}
}
