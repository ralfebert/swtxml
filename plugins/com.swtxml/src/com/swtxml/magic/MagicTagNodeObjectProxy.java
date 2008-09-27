/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.magic;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.ITagDefinition;
import com.swtxml.swt.SwtHandling;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class MagicTagNodeObjectProxy extends TagNode {

	private Object obj;
	private ITagDefinition tag;

	public MagicTagNodeObjectProxy(ITagDefinition tag, TagInformation tagInformation, Object obj) {
		super(tagInformation);
		this.tag = tag;
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "[[[ " + obj + " ]]]";
	}

	@Override
	public <T> T adaptTo(Class<T> type) {
		if (type.isAssignableFrom(Layout.class)) {
			// TODO: again: this is not for object proxies, but for swt widgets
			// only
			if (!(obj instanceof Control)) {
				return null;
			}
			return (T) ((Control) obj).getParent().getLayout();
		} else {
			return super.adaptTo(type);
		}
	}

	@Override
	public void process() {
		for (String name : attributes.keySet()) {
			// TODO: widget vs general class
			if (tag != null && obj instanceof Widget) {
				SwtHandling.WIDGET_PROPERTIES.getProperties(obj.getClass()).getInjector(obj)
						.setPropertyValue(name, attributes.get(name));
			}
		}
	}

}
