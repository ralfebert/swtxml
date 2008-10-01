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
package com.swtxml.swt.types;

import com.swtxml.util.parser.ParseException;
import com.swtxml.util.types.IType;

public class LabelType implements IType<String> {
	public String convert(String value) {
		// TODO: i18n
		if (value.startsWith("%")) {
			throw new ParseException("i18n for string properties is currently unsupported");
		}
		return value;
	}
}