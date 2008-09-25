package com.swtxml.util;

import java.lang.reflect.Method;

public class ReflectorProperty {

	private Method getter;
	private Method setter;

	public ReflectorProperty(Method getter, Method setter) {
		this.getter = getter;
		this.setter = setter;

	}

	public String getName() {
		String n = getter.getName().substring(3);
		return n.substring(0, 1).toLowerCase() + n.substring(1);
	}

}
