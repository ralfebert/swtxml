package com.swtxml.util.properties;

import com.swtxml.util.reflector.IReflectorProperty;

public interface ISetter {

	void apply(IReflectorProperty property, Object obj, String name, String value);

}
