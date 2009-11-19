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
package com.swtxml.events.internal;

import com.swtxml.definition.internal.NamespaceDefinition;

public class EventNamespaceDefinition extends NamespaceDefinition {

	public static final String URI = "http://www.swtxml.com/events";

	public EventNamespaceDefinition() {
		for (String eventName : Events.EVENTS.getAllEventNames()) {
			defineForeignAttribute(new EventForeignAttribute(eventName));
		}
	}
}