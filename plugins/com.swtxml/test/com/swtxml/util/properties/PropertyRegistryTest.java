package com.swtxml.util.properties;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.IInjector;
import com.swtxml.util.properties.IType;
import com.swtxml.util.properties.PropertyMatcher;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.reflector.TestVO;

public class PropertyRegistryTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testPropertyRegistry() {
		TestVO test = new TestVO();

		PropertyRegistry propertyRegistry = new PropertyRegistry(true);

		IType intType = createMock(IType.class);
		IType typeForTestVO = createMock(IType.class);
		IType neverUsedType = createMock(IType.class);

		propertyRegistry.add(new PropertyMatcher(Integer.TYPE), intType);
		propertyRegistry.add(new PropertyMatcher(TestVO.class, PropertyMatcher.ALL_PROPERTIES),
				typeForTestVO);
		propertyRegistry.add(new PropertyMatcher(), neverUsedType);

		expect(intType.convert(test, "5")).andReturn(5);
		expect(typeForTestVO.convert(test, "yaya")).andReturn("yaya2");

		replay(intType, typeForTestVO, neverUsedType);

		ClassProperties<? extends TestVO> properties = propertyRegistry.getProperties(test
				.getClass());

		List<String> propNames = new ArrayList<String>(properties.getProperties().keySet());
		Collections.sort(propNames);
		System.out.println(propNames);
		assertTrue(propNames.contains("counter"));
		assertTrue(propNames.contains("baseText"));

		IInjector injector = properties.getInjector(test);
		injector.setPropertyValue("counter", "5");
		injector.setPropertyValue("baseText", "yaya");

		verify(intType, typeForTestVO, neverUsedType);
		assertEquals(5, test.getCounter());
		assertEquals("yaya2", test.getBaseText());
	}

}
