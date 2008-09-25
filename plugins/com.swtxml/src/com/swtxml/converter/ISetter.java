package com.swtxml.converter;

import com.swtxml.util.IReflectorProperty;

public interface ISetter<T> {

	void set(Object obj, IReflectorProperty property, String value);

}
