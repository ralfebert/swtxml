package com.swtxml.swt.injector;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import com.swtxml.swt.SwtInfo;
import com.swtxml.util.properties.IInjector;

public class SwtInjectorTest {

	@Test
	public void testSwtLayout() {
		Composite composite = new Composite(new Shell(), SWT.NONE);
		IInjector injector = SwtInfo.WIDGET_PROPERTIES.getProperties(composite.getClass())
				.getInjector(composite);
		injector.setPropertyValue("layout", "layout:grid;numColumns:2;");
		assertEquals(2, ((GridLayout) composite.getLayout()).numColumns);
	}

}
