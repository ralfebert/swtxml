package com.swtxml.swt.types;

import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.types.IType;

public class StyleType implements IType<Integer> {

	private ConstantParser constants;

	public StyleType(ConstantParser constants) {
		this.constants = constants;
	}

	public Integer convert(Object obj, String value) {
		return constants.getIntValue(value);
	}

}
