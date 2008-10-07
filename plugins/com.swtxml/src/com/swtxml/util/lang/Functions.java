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
package com.swtxml.util.lang;

import org.apache.commons.lang.ObjectUtils;

/**
 * Common Collection Functions.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class Functions {

	public static final IFunction<Object, String> TO_STRING = new IFunction<Object, String>() {

		public String apply(Object obj) {
			return ObjectUtils.toString(obj);
		}

	};

}
