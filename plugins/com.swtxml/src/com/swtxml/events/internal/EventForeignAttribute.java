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

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.ITagScope;
import com.swtxml.swt.metadata.WidgetTag;
import com.swtxml.util.types.IType;
import com.swtxml.util.types.SimpleTypes;

public class EventForeignAttribute implements IAttributeDefinition, ITagScope {

	public EventForeignAttribute(String name) {
		super();
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public IType<?> getType() {
		return SimpleTypes.STRING;
	}

	public boolean isAllowedIn(ITagDefinition tagDefinition) {
		if (!(tagDefinition instanceof WidgetTag)) {
			return false;
		}

		return SwtEvents.getNamespace().getEvents().getWidgetEvent(
				((WidgetTag) tagDefinition).getWidgetClass(), name) != null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getName() + "]";
	}
}
