/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.parser;

public class XmlParsingException extends RuntimeException {

	public XmlParsingException() {
		super();
	}

	public XmlParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public XmlParsingException(String message) {
		super(message);
	}

	public XmlParsingException(Throwable cause) {
		super(cause);
	}

}
