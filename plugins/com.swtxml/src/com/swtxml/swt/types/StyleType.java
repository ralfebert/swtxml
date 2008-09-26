package com.swtxml.swt.types;

import java.util.Collection;

import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.parser.Splitter;
import com.swtxml.util.types.IEnumeratedType;
import com.swtxml.util.types.IType;

public class StyleType implements IType<Integer>, IEnumeratedType {

	private ConstantParser constants;

	public StyleType(ConstantParser constants) {
		this.constants = constants;
	}

	public Integer convert(Object obj, String value) {
		return constants.getIntValue(value);
	}

	public Collection<String> getEnumValues() {
		return constants.getConstants();
	}

	public Splitter getSplitRule() {
		return ConstantParser.MULTIPLE;
	}

}
