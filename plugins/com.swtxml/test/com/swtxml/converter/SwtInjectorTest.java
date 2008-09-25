package com.swtxml.converter;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Test;

public class SwtInjectorTest {
	private IIdResolver idResolver;

	@Before
	public void setup() {
		idResolver = createMock(IIdResolver.class);
	}

	@Test
	public void testSwtLayout() {
		Composite composite = new Composite(new Shell(), SWT.NONE);
		IInjector injector = Injectors.createSwtInjector(idResolver).getInjector(composite, false);
		injector.setPropertyValue("layout", "layout:grid;numColumns:2;");
		assertEquals(2, ((GridLayout) composite.getLayout()).numColumns);
	}

}
