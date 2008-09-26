/**
 * 
 */
package com.swtxml.util.properties;

public interface IConverter<T> {
	T convert(Object obj, String value);
}