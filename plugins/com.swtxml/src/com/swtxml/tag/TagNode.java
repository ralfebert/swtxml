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

import com.swtxml.parser.TagLibraryException;

public abstract class TagNode extends TagInformation {

	private List<TagNode> children = new ArrayList<TagNode>();

	public TagNode(TagInformation tagInfo) {
		super(tagInfo);
		this.getDocument().register(this);
	}

	public void process() {
		for (TagAttribute attr : getAttributes()) {
			if (attr.isProcessed()) {
				continue;
			}
			if (attr.isLocal()) {
				processAttribute(attr);
			} else {
				attr.getTagLibrary().foreignAttribute(this, attr);
			}

		}
		checkMissingAttributes();
	}

	protected abstract void processAttribute(TagAttribute attr);

	private void checkMissingAttributes() {
		for (TagAttribute attr : getAttributes()) {
			if (!attr.isProcessed()) {
				throw new TagLibraryException(this, "Unprocessed attribute: " + attr);
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	public List<TagNode> getChildren() {
		return children;
	}

}
