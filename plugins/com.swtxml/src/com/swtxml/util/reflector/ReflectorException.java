/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.util.reflector;

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
