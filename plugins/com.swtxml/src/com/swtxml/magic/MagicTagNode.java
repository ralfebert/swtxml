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

import com.swtxml.swt.SwtHelper;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class MagicTagNode extends TagNode {

	public MagicTagNode(TagInformation tagInfo) {
		super(tagInfo);
	}

	@Override
	protected final void processAttribute(TagAttribute attr) {
		SwtHelper.injectAttribute(this, this, attr, true);
		attr.setProcessed();
	}

}
