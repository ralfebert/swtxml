package com.swtxml.util.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.swtxml.util.adapter.IAdaptable;

public class ContextTest {

	@Test
	public void testAdaptTo() {
		assertNull(Context.adaptTo(String.class));
		Context.addAdapter(new IAdaptable() {

			@SuppressWarnings("unchecked")
			public <A> A adaptTo(Class<A> adapter) {
				if (adapter.isAssignableFrom(String.class)) {
					return (A) "test";
				}
				return null;
			}

		});
		assertEquals("test", Context.adaptTo(String.class));
		Context.clear();
		assertNull(Context.adaptTo(String.class));
	}

}
