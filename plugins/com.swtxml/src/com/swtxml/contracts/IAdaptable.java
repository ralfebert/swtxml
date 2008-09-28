package com.swtxml.contracts;

public interface IAdaptable {

	public <A> A adaptTo(Class<A> adapterClass);

}
