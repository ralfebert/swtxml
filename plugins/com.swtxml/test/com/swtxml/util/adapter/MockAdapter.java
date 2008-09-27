package com.swtxml.util.adapter;

public class MockAdapter implements IAdaptable {

	private Object obj;

	public MockAdapter(Object obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public <A> A adaptTo(Class<A> adapterClass) {
		if (adapterClass.isAssignableFrom(obj.getClass())) {
			return (A) obj;
		}
		return null;
	}

}
