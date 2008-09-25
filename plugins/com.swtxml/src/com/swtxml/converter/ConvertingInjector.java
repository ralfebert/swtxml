package com.swtxml.converter;

import com.swtxml.util.IReflectorProperty;
import com.swtxml.util.ReflectorBean;
import com.swtxml.util.ReflectorException;

public class ConvertingInjector {

	private final ReflectorBean reflector;
	private IConverterLibrary converterLibrary;
	private Object obj;

	public ConvertingInjector(Object obj, IConverterLibrary converterLibrary,
			boolean includePublicFields) {
		this.obj = obj;
		this.reflector = new ReflectorBean(obj.getClass(), includePublicFields);
		this.converterLibrary = converterLibrary;
	}

	public void setPropertyValue(String name, String value) {
		IReflectorProperty property = reflector.getProperty(name);
		String objClassName = obj.getClass().getSimpleName();
		if (property == null) {
			throw new ReflectorException("Unknown attribute: " + name + " for " + objClassName);
		}
		IConverter<?> converter = converterLibrary.forProperty(name, property.getType());
		if (converter == null) {
			throw new ReflectorException("No suitable converter found for " + name);
		}
		try {
			property.set(obj, converter.convert(value));
		} catch (Exception e) {
			throw new ReflectorException("Invalid value for " + objClassName + "." + name + ": \""
					+ value + "\" (" + e.getMessage() + ")", e);
		}
	}

}
