package com.swtxml.util.injector;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;

import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.IConverter;
import com.swtxml.util.properties.IInjector;
import com.swtxml.util.properties.ISetter;
import com.swtxml.util.properties.PropertyMatcher;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.reflector.TestVO;

public class InjectorTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testInjectorDefinition() {
		TestVO test = new TestVO();

		PropertyRegistry propertyRegistry = new PropertyRegistry(true);

		IConverter converter = createMock(IConverter.class);
		ISetter setter1 = createMock(ISetter.class);
		ISetter setter2 = createMock(ISetter.class);

		propertyRegistry.add(new PropertyMatcher(Integer.TYPE), converter);
		propertyRegistry.add(new PropertyMatcher(TestVO.class), setter1);
		propertyRegistry.add(new PropertyMatcher(), setter2);

		expect(converter.convert(test, "5")).andReturn(5);
		setter2.apply(EasyMock.<IReflectorProperty> anyObject(), eq(test), eq("baseText"),
				eq("yaya"));

		replay(converter, setter1, setter2);

		ClassProperties<? extends TestVO> properties = propertyRegistry.getProperties(test
				.getClass());

		Collection<String> propNames = properties.getProperties().keySet();
		assertTrue(propNames.contains("counter"));
		assertTrue(propNames.contains("baseText"));

		IInjector injector = properties.getInjector(test);
		injector.setPropertyValue("counter", "5");
		injector.setPropertyValue("baseText", "yaya");

		verify(converter, setter1, setter2);
		assertEquals(5, test.getCounter());
	}

}
