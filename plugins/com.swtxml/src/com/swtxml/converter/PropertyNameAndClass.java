/**
 * 
 */
package com.swtxml.converter;

public class PropertyNameAndClass {
	private final String propertyName;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PropertyNameAndClass other = (PropertyNameAndClass) obj;
		if (clazz == null) {
			if (other.clazz != null) {
				return false;
			}
		} else if (!clazz.equals(other.clazz)) {
			return false;
		}
		if (propertyName == null) {
			if (other.propertyName != null) {
				return false;
			}
		} else if (!propertyName.equals(other.propertyName)) {
			return false;
		}
		return true;
	}

	public PropertyNameAndClass(String propertyName, Class<?> clazz) {
		super();
		this.propertyName = propertyName;
		this.clazz = clazz;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	private final Class<?> clazz;
}