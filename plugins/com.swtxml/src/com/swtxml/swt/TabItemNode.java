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

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabItem;

import com.swtxml.magic.MagicTagNodeObjectProxy;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class TabItemNode extends MagicTagNodeObjectProxy {

	public TabItemNode(TagInformation tagInformation, Object obj) {
		super(tagInformation, obj);
	}

	@Override
	public void process() {
		super.process();
		Control control = null;
		for (TagNode children : getChildren()) {
			Control childNodeControl = children.get(Control.class);
			if (childNodeControl != null) {
				if (control != null) {
					throw new TagLibraryException(this,
							"TabItems may have only one control inside!");
				} else {
					control = childNodeControl;
				}
			}
		}
		if (control != null) {
			get(TabItem.class).setControl(control);
		}
	}

}
