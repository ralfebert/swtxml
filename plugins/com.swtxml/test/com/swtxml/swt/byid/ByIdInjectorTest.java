package com.swtxml.swt.byid;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.swtxml.contracts.IIdResolver;

public class ByIdInjectorTest {

	@Test
	public void testInject() {

		IIdResolver idResolver = createMock(IIdResolver.class);
		expect(idResolver.getById("baseNumber", Integer.class)).andReturn(5);
		expect(idResolver.getById("test", String.class)).andReturn("Hallo");

		ByIdView view = new ByIdView();

		replay(idResolver);
		new ByIdInjector().inject(view, idResolver);

		assertEquals(5, view.getBaseNumberX());
		assertEquals("Hallo", view.getTestX());
		verify(idResolver);
	}

}
