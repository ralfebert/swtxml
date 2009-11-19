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
import com.swtxml.events.internal.EventNamespaceDefinition;
import com.swtxml.events.internal.Events;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.metadata.SwtNamespace;

/**
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class DefaultNamespaceResolver implements INamespaceResolver {

	public INamespaceDefinition resolveNamespace(String uri) {
		if (SwtNamespace.URI.equals(uri)) {
			return SwtInfo.NAMESPACE;
		}
		if (EventNamespaceDefinition.URI.equals(uri)) {
			return Events.NAMESPACE;
		}
		return null;
	}

}
