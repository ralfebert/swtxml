package com.swtxml.util.properties;

public class PropertiesException extends RuntimeException {

	public PropertiesException() {
	}

	public PropertiesException(String message) {
		super(message);
	}

	public PropertiesException(Throwable cause) {
		super(cause);
	}

	public PropertiesException(String message, Throwable cause) {
		super(message, cause);
	}

}
