/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.util.properties;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.swtxml.util.reflector.PublicFields;
import com.swtxml.util.reflector.TestVO;
import com.swtxml.util.types.IType;

public class PropertyRegistryTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testPropertyRegistry() {
		TestVO test = new TestVO();

		PropertyRegistry propertyRegistry = new PropertyRegistry(PublicFields.INCLUDE);

		IType intType = createMock(IType.class);
		IType typeForTestVO = createMock(IType.class);
		IType neverUsedType = createMock(IType.class);

		propertyRegistry.add(new PropertyMatcher(Integer.TYPE), intType);
		propertyRegistry.add(new PropertyMatcher(TestVO.class, PropertyMatcher.ALL_PROPERTIES),
				typeForTestVO);
		propertyRegistry.add(new PropertyMatcher(), neverUsedType);

		expect(intType.convert("5")).andReturn(5);
		expect(typeForTestVO.convert("yaya")).andReturn("yaya2");

		replay(intType, typeForTestVO, neverUsedType);

		ClassProperties<? extends TestVO> properties = propertyRegistry.getProperties(test
				.getClass());

		List<String> propNames = new ArrayList<String>(properties.getProperties().keySet());
		Collections.sort(propNames);
		assertEquals("[basePublicText, baseText, counter, publicText, text]", propNames.toString());

		IInjector injector = properties.getInjector(test);
		injector.setPropertyValue("counter", "5");
		injector.setPropertyValue("baseText", "yaya");

		verify(intType, typeForTestVO, neverUsedType);
		assertEquals(5, test.getCounter());
		assertEquals("yaya2", test.getBaseText());
	}

}
