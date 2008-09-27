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

import com.swtxml.metadata.ITag;
import com.swtxml.swt.SwtHandling;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class MagicTagNodeObjectProxy extends TagNode {

	private Object obj;
	private ITag tag;

	public MagicTagNodeObjectProxy(ITag tag, TagInformation tagInformation, Object obj) {
		super(tagInformation);
		this.tag = tag;
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "[[[ " + obj + " ]]]";
	}

	@Override
	public <T> T get(Class<T> type) {
		if (TagNode.class.isAssignableFrom(type)) {
			return (T) this;
			// TODO: again: this is not for object proxies, but for swt widgets
			// only
		} else if (type.isAssignableFrom(Layout.class)) {
			if (!(obj instanceof Control)) {
				return null;
			}
			return (T) ((Control) obj).getParent().getLayout();
		} else {
			return (T) ((type.isAssignableFrom(obj.getClass())) ? obj : null);
		}
	}

	@Override
	public void process() {
		for (String name : attributes.keySet()) {
			// TODO: widget vs general class
			if (tag != null && obj instanceof Widget) {
				SwtHandling.createSwtProperties(getDocument()).getProperties(obj.getClass())
						.getInjector(obj).setPropertyValue(name, attributes.get(name));
			}
		}
	}

}
