package com.swtxml.swt.injector;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.swt.SwtHandling;
import com.swtxml.swt.properties.IIdResolver;
import com.swtxml.util.properties.IInjector;

public class SwtInjectorTest {
	private IIdResolver idResolver;

	@Before
	public void setup() {
		idResolver = createMock(IIdResolver.class);
	}

	@Test
	public void testSwtLayout() {
		Composite composite = new Composite(new Shell(), SWT.NONE);
		IInjector injector = SwtHandling.createSwtProperties(idResolver).getProperties(
				composite.getClass()).getInjector(composite);
		injector.setPropertyValue("layout", "layout:grid;numColumns:2;");
		assertEquals(2, ((GridLayout) composite.getLayout()).numColumns);
	}

}
