package com.swtxml.converter;

import com.swtxml.util.IReflectorProperty;

public class ConvertingSetter implements ISetter {

	private IConverter<?> converter;

	public ConvertingSetter(IConverter<?> converter) {
		this.converter = converter;
	}

	public boolean apply(IReflectorProperty property, Object obj, String name, String value) {
		property.set(obj, converter.convert(value));
		return true;
	}

}
