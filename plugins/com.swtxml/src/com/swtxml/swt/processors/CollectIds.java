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
package com.swtxml.swt.processors;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.contracts.IAdaptable;
import com.swtxml.contracts.IIdResolver;
import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;

public class CollectIds implements ITagProcessor, IIdResolver, IAdaptable {

	private Map<String, Tag> tagsById = new HashMap<String, Tag>();

	public void process(Tag tag) {
		String id = tag.slurpAttribute("id");
		if (id != null) {
			tagsById.put(id, tag);
		}
	}

	public <T> T getById(String id, Class<T> clazz) {
		Tag node = tagsById.get(id);
		if (node == null) {
			return null;
		}

		return node.adaptTo(clazz);
	}

	@SuppressWarnings("unchecked")
	public <A> A adaptTo(Class<A> type) {
		if (type.isAssignableFrom(IIdResolver.class)) {
			return (A) this;
		}
		return null;
	}

	@Override
	public String toString() {
		return "CollectIds[" + tagsById + "]";
	}

}
