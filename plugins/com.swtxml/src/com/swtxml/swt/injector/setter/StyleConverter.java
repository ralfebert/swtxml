package com.swtxml.swt.injector.setter;

import com.swtxml.util.injector.IConverter;
import com.swtxml.util.parser.ConstantParser;

public class StyleConverter implements IConverter<Integer> {

	private ConstantParser constants;

	public StyleConverter(ConstantParser constants) {
		this.constants = constants;
	}

	public Integer convert(String value) {
		return constants.getIntValue(value);
	}

}
