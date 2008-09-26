package com.swtxml.swt.properties.setter;

import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.properties.IConverter;

public class StyleConverter implements IConverter<Integer> {

	private ConstantParser constants;

	public StyleConverter(ConstantParser constants) {
		this.constants = constants;
	}

	public Integer convert(String value) {
		return constants.getIntValue(value);
	}

}
