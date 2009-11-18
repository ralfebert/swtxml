/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.swt.visitor;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.adapter.IIdResolver;
import com.swtxml.tinydom.ITagVisitor;
import com.swtxml.tinydom.Tag;

public class CollectIds implements ITagVisitor, IIdResolver, IAdaptable {

	private Map<String, Tag> tagsById = new HashMap<String, Tag>();

	public void visit(Tag tag) {
		String id = tag.getAttributeValue("id");
		if (id != null) {
			tagsById.put(id, tag);
		}
	}

	public <T> T getById(String id, Class<T> clazz) {
		Tag node = tagsById.get(id);
		return (node != null) ? node.getAdapter(clazz) : null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> type) {
		if (type.isAssignableFrom(IIdResolver.class)) {
			return (T) this;
		}
		return null;
	}

	@Override
	public String toString() {
		return "CollectIds[" + tagsById + "]";
	}

}
