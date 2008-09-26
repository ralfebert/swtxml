package com.swtxml.util.types;

import java.util.Collection;

import com.swtxml.util.parser.Splitter;

public interface IEnumeratedType {

	public Collection<String> getEnumValues();

	public Splitter getSplitRule();

}
