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
import com.swtxml.metadata.ITag;
import com.swtxml.metadata.SwtTagRegistry;
import com.swtxml.metadata.SwtWidgetBuilder;
import com.swtxml.parser.IAttributeConverter;
import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class SwtWidgetTagLibrary implements ITagLibrary, IAttributeConverter {

	private SwtTagRegistry registry = new SwtTagRegistry();

	public TagNode tag(TagInformation tagInfo) {

		ITag tag = registry.getTag(tagInfo.getTagName());
		if (tag == null) {
			throw new TagLibraryException(tagInfo, "Unknown tag: " + tagInfo.getTagName());
		}
		SwtWidgetBuilder builder = tag.adaptTo(SwtWidgetBuilder.class);
		if (builder == null) {
			throw new TagLibraryException(tagInfo, "No builder known for " + tag);
		}

		try {
			Integer style = SwtConstants.SWT.getIntValue(tagInfo.processAttribute("style"));
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

	public Object convert(TagInformation node, TagAttribute attr, Class<?> destClass) {
		throw new RuntimeException("DENKSTE!");
	}

	public void foreignAttribute(TagNode node, TagAttribute attr) {
		// nothing to do
	}

}