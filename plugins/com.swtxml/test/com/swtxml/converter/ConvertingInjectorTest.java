package com.swtxml.converter;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.swtxml.util.TestVO;

public class ConvertingInjectorTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testConvertingInjector() {
		TestVO test = new TestVO();
		IConverter converter = createMock(IConverter.class);
		IConverterLibrary converterLibrary = createMock(IConverterLibrary.class);

		expect(converter.convert("5")).andReturn(5);
		expect(converterLibrary.forProperty(test, "counter", Integer.TYPE)).andReturn(converter);

		replay(converterLibrary, converter);

		ConvertingInjector injector = new ConvertingInjector(test, converterLibrary, true);
		injector.setPropertyValue("counter", "5");

		verify(converterLibrary, converter);
		assertEquals(5, test.getCounter());
	}
}
