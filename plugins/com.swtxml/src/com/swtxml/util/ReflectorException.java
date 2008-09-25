package com.swtxml.util;

import java.lang.reflect.InvocationTargetException;

public class ReflectorException extends RuntimeException {

	public ReflectorException() {
	}

	public ReflectorException(String message) {
		super(message);
	}

	public ReflectorException(Throwable cause) {
		super((cause instanceof InvocationTargetException) ? ((InvocationTargetException) cause)
				.getTargetException() : cause);
	}

	public ReflectorException(String message, Throwable cause) {
		super(message,
				(cause instanceof InvocationTargetException) ? ((InvocationTargetException) cause)
						.getTargetException() : cause);
	}

}
