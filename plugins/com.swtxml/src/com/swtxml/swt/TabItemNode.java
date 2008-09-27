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

import com.swtxml.definition.ITagDefinition;
import com.swtxml.magic.MagicTagNodeObjectProxy;
import com.swtxml.tag.TagInformation;

public class TabItemNode extends MagicTagNodeObjectProxy {

	public TabItemNode(ITagDefinition tag, TagInformation tagInformation, Object obj) {
		super(tag, tagInformation, obj);
	}

	@Override
	public void process() {
		super.process();
	}
}
