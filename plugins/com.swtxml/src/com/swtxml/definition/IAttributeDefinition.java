package com.swtxml.definition;

import com.swtxml.util.types.IType;

public interface IAttributeDefinition extends Comparable<IAttributeDefinition> {

	String getName();

	IType<?> getType();

}
