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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.swt.metadata.WidgetBuilder;
import com.swtxml.swt.metadata.WidgetTag;
import com.swtxml.tag.TagInformation;

public class SwtWidgetTagLibrary implements ITagLibrary {

	public TagInformation tag(TagInformation tagInfo) {

		WidgetTag tag = (WidgetTag) tagInfo.getTagDefinition();
		WidgetBuilder builder = new WidgetBuilder(tag);

		try {
			Integer style = SwtHandling.SWT.getIntValue(tagInfo.getAttribute("style"));
			Composite parent = (Composite) tagInfo.parentRecursiveAdaptTo(builder.getParentClass());
			Widget widget = builder.build(parent, style == null ? SWT.NONE : style);
			tagInfo.makeAdaptable(widget);
			return tagInfo;
		} catch (Exception e) {
			throw new TagLibraryException(tagInfo, e);
		}

	}

}