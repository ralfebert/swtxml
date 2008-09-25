package com.swtxml.converter;

import com.swtxml.util.IReflectorProperty;

public interface ISetter {

	boolean apply(IReflectorProperty property, Object obj, String name, String value);

}
