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
package com.swtxml.i18n;

/**
 * Implementations of ILabelTranslator are able to provide translated Strings
 * for keys for the current locale.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public interface ILabelTranslator {

	public String translate(String key);

}
