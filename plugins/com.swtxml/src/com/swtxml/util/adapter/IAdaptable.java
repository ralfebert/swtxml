package com.swtxml.util.adapter;

public interface IAdaptable {

	public <A> A adaptTo(Class<A> adapterClass);

}
