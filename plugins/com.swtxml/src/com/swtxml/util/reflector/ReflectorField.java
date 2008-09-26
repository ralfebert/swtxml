package com.swtxml.util.reflector;

import java.lang.reflect.Field;

public class ReflectorField implements IReflectorProperty {

	private Field field;

	public ReflectorField(Field field) {
		this.field = field;
	}

	public Object get(Object obj) {
		try {
			return field.get(obj);
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	public String getName() {
		return field.getName();
	}

	public Class<?> getType() {
		return field.getType();
	}

	public void set(Object obj, Object value) {
		try {
			field.set(obj, value);
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	@Override
	public String toString() {
		return "ReflectorField[" + getName() + "]";
	}

}
