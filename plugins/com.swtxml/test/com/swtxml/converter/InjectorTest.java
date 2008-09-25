package com.swtxml.converter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
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
		ISetter setter1 = createMock(ISetter.class);
		ISetter setter2 = createMock(ISetter.class);

		injectorDefinition.addConverter(new PropertyMatcher(Integer.TYPE), converter);
		injectorDefinition.addSetter(new PropertyMatcher(), setter1);
		injectorDefinition.addSetter(new PropertyMatcher(), setter2);
		injectorDefinition.addSetter(new PropertyMatcher(), setter1);

		expect(converter.convert("5")).andReturn(5);
		expect(
				setter1.apply(EasyMock.<IReflectorProperty> anyObject(), eq(test), eq("baseText"),
						eq("yaya"))).andReturn(false);
		expect(
				setter2.apply(EasyMock.<IReflectorProperty> anyObject(), eq(test), eq("baseText"),
						eq("yaya"))).andReturn(true);

		replay(converter, setter1, setter2);

		IInjector injector = injectorDefinition.getInjector(test, true);
		injector.setPropertyValue("counter", "5");
		injector.setPropertyValue("baseText", "yaya");

		verify(converter, setter1, setter2);
		assertEquals(5, test.getCounter());
	}

}
