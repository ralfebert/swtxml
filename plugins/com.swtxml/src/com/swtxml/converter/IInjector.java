package com.swtxml.converter;

import java.util.Map;

public interface IInjector {

	public abstract void setPropertyValue(String name, String value);

	public abstract void setPropertyValues(Map<String, String> values);

}