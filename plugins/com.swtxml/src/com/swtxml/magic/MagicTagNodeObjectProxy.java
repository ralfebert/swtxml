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

import org.eclipse.swt.widgets.Widget;

import com.swtxml.converter.Injectors;
import com.swtxml.metadata.ITag;
import com.swtxml.metadata.SwtTagAttribute;
import com.swtxml.tag.TagAttribute;
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
		} else {
			return (T) ((type.isAssignableFrom(obj.getClass())) ? obj : null);
		}
	}

	@Override
	// TODO: move processAttribute out
	protected void processAttribute(TagAttribute attr) {
		if (tag != null && obj instanceof Widget) {
			SwtTagAttribute tagAttr = (SwtTagAttribute) tag.getAttribute(attr.getName());
			Injectors.createSwtInjector(getDocument()).getInjector(obj, false).setPropertyValue(
					attr.getName(), attr.getValue());
		}
		attr.setProcessed();
	}

}
