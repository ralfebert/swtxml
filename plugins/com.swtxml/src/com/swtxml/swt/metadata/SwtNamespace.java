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

import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.internal.NamespaceDefinition;
import com.swtxml.swt.SwtInfo;

public class SwtNamespace extends NamespaceDefinition {

	private static final SwtNamespace namespace = new SwtNamespace();

	public static INamespaceDefinition getNamespace() {
		return namespace;
	}

	public SwtNamespace() {
		super("http://www.swtxml.com/swt");
		for (String className : SwtInfo.WIDGETS.getWidgetClassNames()) {
			Class<? extends Widget> widgetClass = SwtInfo.WIDGETS.getWidgetClass(className);
			if (Modifier.isAbstract(widgetClass.getModifiers())) {
				continue;
			}
			defineTag(new WidgetTag(widgetClass));
		}
	}

	@Override
	public WidgetTag getTag(String name) {
		return (WidgetTag) super.getTag(name);
	}

}
