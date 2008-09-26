package com.swtxml.util.properties;

import com.swtxml.util.reflector.IReflectorProperty;

public class ConvertingSetter implements ISetter {

	private IConverter<?> converter;

	public ConvertingSetter(IConverter<?> converter) {
		this.converter = converter;
	}

	public void apply(IReflectorProperty property, Object obj, String name, String value) {
		property.set(obj, converter.convert(value));
	}

}