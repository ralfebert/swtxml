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
package com.swtxml.events;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagScope;
import com.swtxml.events.internal.SwtEvents;
import com.swtxml.swt.SwtInfo;

public class EventNamespaceDefinitionTest {

	@Test
	public void testDefinitionPlausible() {
		IAttributeDefinition widgetSelected = SwtEvents.getNamespace().getForeignAttribute(
				"widgetSelected");
		assertNotNull(widgetSelected);
		assertTrue(((ITagScope) widgetSelected).isAllowedIn(SwtInfo.NAMESPACE.getTag("Button")));
		assertFalse(((ITagScope) widgetSelected).isAllowedIn(SwtInfo.NAMESPACE.getTag("Control")));

		IAttributeDefinition focusGained = SwtEvents.getNamespace().getForeignAttribute(
				"focusGained");
		assertNotNull(focusGained);
		assertTrue(((ITagScope) focusGained).isAllowedIn(SwtInfo.NAMESPACE.getTag("Button")));
		assertFalse(((ITagScope) focusGained).isAllowedIn(SwtInfo.NAMESPACE.getTag("Control")));
	}

}
