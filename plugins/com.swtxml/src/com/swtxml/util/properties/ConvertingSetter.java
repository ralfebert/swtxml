package com.swtxml.util.properties;

import com.swtxml.util.reflector.IReflectorProperty;

public class ConvertingSetter implements ISetter {

	private IType<?> converter;

	public ConvertingSetter(IType<?> converter) {
		this.converter = converter;
	}

	public void apply(IReflectorProperty property, Object obj, String name, String value) {
		property.set(obj, converter.convert(obj, value));
	}

}