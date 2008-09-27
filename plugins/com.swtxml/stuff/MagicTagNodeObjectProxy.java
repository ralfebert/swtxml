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

import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class MagicTagNodeObjectProxy extends TagNode {

	private Object obj;

	public MagicTagNodeObjectProxy(TagInformation tagInformation, Object obj) {
		super(tagInformation);
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "[[[ " + obj + " ]]]";
	}

	@Override
	public void process() {
	}

}
