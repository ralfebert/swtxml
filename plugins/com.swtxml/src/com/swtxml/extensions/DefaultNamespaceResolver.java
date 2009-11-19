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
package com.swtxml.extensions;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.INamespaceResolver;
import com.swtxml.events.internal.SwtEvents;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.metadata.SwtNamespace;

/**
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class DefaultNamespaceResolver implements INamespaceResolver {

	public INamespaceDefinition resolveNamespace(String uri) {
		// TODO: keep in map
		if (SwtNamespace.getNamespace().getUri().equals(uri)) {
			return SwtInfo.NAMESPACE;
		}
		if (SwtEvents.getNamespace().getUri().equals(uri)) {
			return SwtEvents.getNamespace();
		}
		return null;
	}

}
