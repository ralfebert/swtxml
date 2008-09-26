/**
 * 
 */
package com.swtxml.util.injector;

public interface IConverter<T> {
	T convert(String value);
}