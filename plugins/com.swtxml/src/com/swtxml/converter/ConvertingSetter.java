package com.swtxml.converter;

import com.swtxml.util.IReflectorProperty;

public class ConvertingSetter<T> implements ISetter<T> {

	private IConverter<T> converter;

	public ConvertingSetter(IConverter<T> converter) {
		this.converter = converter;
	}

	public void set(Object obj, IReflectorProperty property, String value) {
		property.set(obj, converter.convert(value));
	}

}
