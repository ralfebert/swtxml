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
package com.swtxml.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.magic.MagicTagNodeObjectProxy;
import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.swt.metadata.WidgetTag;
import com.swtxml.swt.metadata.WidgetBuilder;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class SwtWidgetTagLibrary implements ITagLibrary {

	private SwtNamespace registry = new SwtNamespace();

	public TagNode tag(TagInformation tagInfo) {

		WidgetTag tag = registry.getTags().get(tagInfo.getTagName());
		if (tag == null) {
			throw new TagLibraryException(tagInfo, "Unknown tag: " + tagInfo.getTagName());
		}
		WidgetBuilder builder = new WidgetBuilder(tag);

		try {
			Integer style = SwtHandling.SWT.getIntValue(tagInfo.getAttribute("style"));
			Class<?> parentClass = builder.getParentClass();
			Widget widget = builder.build(tagInfo.findParentRecursive(parentClass),
					style == null ? SWT.NONE : style);
			if (widget instanceof TabItem) {
				return new TabItemNode(tag, tagInfo, widget);
			}
			return new MagicTagNodeObjectProxy(tag, tagInfo, widget);
		} catch (Exception e) {
			throw new TagLibraryException(tagInfo, e);
		}

	}

	public void foreignAttribute(TagNode node, String name, String value) {
		// nothing to do
	}

}