package com.swtxml.converter;

import org.eclipse.swt.SWT;

import com.swtxml.swt.ConstantConverter;

public class StyleConverter implements IConverter<Integer> {

	private final static ConstantConverter swtConstantConverter = new ConstantConverter(SWT.class);

	public Integer convert(String value) {
		return swtConstantConverter.getIntValue(value);
	}

}
