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
package com.swtxml.tag;

import java.util.ArrayList;
import java.util.List;

import com.swtxml.util.adapter.IAdaptable;

public abstract class TagNode extends TagInformation implements IAdaptable {

	private List<TagNode> children = new ArrayList<TagNode>();

	public TagNode(TagInformation tagInfo) {
		super(tagInfo);
		this.getDocument().register(this);
	}

	public void process() {
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	public List<TagNode> getChildren() {
		return children;
	}

	public <A> A adaptTo(Class<A> adapterClass) {
		return get(adapterClass);
	}

}
