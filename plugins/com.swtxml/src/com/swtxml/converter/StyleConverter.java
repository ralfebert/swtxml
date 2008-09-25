package com.swtxml.converter;

import com.swtxml.swt.SwtConstants;

public class StyleConverter implements IConverter<Integer> {

	private SwtConstants constants;

	public StyleConverter(SwtConstants constants) {
		this.constants = constants;
	}

	public Integer convert(String value) {
		return constants.getIntValue(value);
	}

}
