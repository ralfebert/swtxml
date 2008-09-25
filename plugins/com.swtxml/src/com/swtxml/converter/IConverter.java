/**
 * 
 */
package com.swtxml.converter;

public interface IConverter<T> {
	T convert(String value);
}