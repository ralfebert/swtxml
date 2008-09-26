package com.swtxml.metadata;

import com.swtxml.util.types.IType;

public interface IAttribute extends Comparable<IAttribute> {

	String getName();

	IType<?> getType();

}
