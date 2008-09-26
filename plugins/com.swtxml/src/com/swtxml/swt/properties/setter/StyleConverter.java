package com.swtxml.swt.properties.setter;

import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.properties.IType;

public class StyleConverter implements IType<Integer> {

	private ConstantParser constants;

	public StyleConverter(ConstantParser constants) {
		this.constants = constants;
	}

	public Integer convert(Object obj, String value) {
		return constants.getIntValue(value);
	}

}
