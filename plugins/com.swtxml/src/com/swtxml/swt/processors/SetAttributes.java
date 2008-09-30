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
package com.swtxml.swt.processors;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.parser.ParseException;

public class SetAttributes implements ITagProcessor {

	public void process(Tag tag) {
		Widget widget = tag.adaptTo(Widget.class);
		if (widget == null) {
			return;
		}

		if (widget instanceof Control) {
			Composite parent = ((Control) widget).getParent();
			if (parent != null && parent.getLayout() != null) {
				// TODO: try not to store an extra reference
				tag.makeAdaptable(parent.getLayout());
			}
		}

		if (widget instanceof TabItem) {
			List<Control> controlChildren = tag.adaptChildren(Control.class);
			if (controlChildren.size() > 1) {
				throw new ParseException("TabItems may have only one nested Control! (is: "
						+ controlChildren + ")");
			}
			if (controlChildren.size() == 1) {
				((TabItem) widget).setControl(controlChildren.get(0));
			}
		}

		for (IAttributeDefinition attr : tag.getAttributes()) {
			SwtInfo.WIDGET_PROPERTIES.getProperties(widget.getClass()).getInjector(widget)
					.setPropertyValue(attr.getName(), tag.getAttribute(attr));
		}
	}

}
