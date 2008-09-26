package com.swtxml.util.injector;

import com.swtxml.util.reflector.IReflectorProperty;

public interface ISetter {

	boolean apply(IReflectorProperty property, Object obj, String name, String value);

}
