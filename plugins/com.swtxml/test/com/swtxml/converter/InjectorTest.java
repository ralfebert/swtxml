package com.swtxml.converter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import com.swtxml.util.IReflectorProperty;
import com.swtxml.util.TestVO;

public class InjectorTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testInjectorDefinition() {
		TestVO test = new TestVO();

		InjectorDefinition injectorDefinition = new InjectorDefinition();

		IConverter converter = createMock(IConverter.class);
		ISetter setter = createMock(ISetter.class);

		injectorDefinition.addConverter(converter, null, null, Integer.TYPE);
		injectorDefinition.addSetter(setter, null, "baseText");

		expect(converter.convert("5")).andReturn(5);
		setter.set(eq(test), EasyMock.<IReflectorProperty> anyObject(), eq("yaya"));

		replay(converter, setter);

		IInjector injector = injectorDefinition.getInjector(test, true);
		injector.setPropertyValue("counter", "5");
		injector.setPropertyValue("baseText", "yaya");

		verify(converter, setter);
		assertEquals(5, test.getCounter());
	}

	@Test
	public void testSwtLayout() {
		Composite composite = new Composite(new Shell(), SWT.NONE);
		IInjector injector = Injectors.getSwt().getInjector(composite, false);
		injector.setPropertyValue("layout", "layout:grid;numColumns:2;");
		assertEquals(2, ((GridLayout) composite.getLayout()).numColumns);
	}
}
