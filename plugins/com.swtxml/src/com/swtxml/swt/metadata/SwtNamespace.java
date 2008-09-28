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
package com.swtxml.swt.metadata;

import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.DefinitionException;
import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.swt.SwtInfo;

public class SwtNamespace implements INamespaceDefinition {

	private Map<String, WidgetTag> tagsByName = new HashMap<String, WidgetTag>();

	public SwtNamespace() {
		for (String className : SwtInfo.WIDGETS.getWidgetClassNames()) {
			Class<? extends Widget> widgetClass = SwtInfo.WIDGETS.getWidgetClass(className);
			if (Modifier.isAbstract(widgetClass.getModifiers())) {
				continue;
			}
			WidgetTag tag = new WidgetTag(widgetClass);
			WidgetTag existingTag = tagsByName.get(tag.getName());
			if (existingTag != null) {
				throw new DefinitionException("Tag naming conflict between " + tag + " and "
						+ existingTag + "!");
			}
			tagsByName.put(tag.getName(), tag);
		}
	}

	public WidgetTag getTag(String name) {
		return tagsByName.get(name);
	}

	public Set<String> getTagNames() {
		return Collections.unmodifiableSet(tagsByName.keySet());
	}

}
