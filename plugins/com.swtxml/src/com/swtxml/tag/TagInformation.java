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

import java.util.Map;

import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.util.adapter.IAdaptable;

public class TagInformation implements IAdaptable {

	private final Document document;

	private final ITagLibrary tagLibrary;
	private final TagNode parent;
	private final String tagName;
	private final String locationInfo;
	private final int level;
	protected final Map<String, String> attributes;

	public TagInformation(Document document, ITagLibrary tagLibrary, TagNode parent,
			String tagName, String locationInfo, int level, Map<String, String> attributes) {
		this.document = document;
		this.tagLibrary = tagLibrary;
		this.parent = parent;
		this.tagName = tagName;
		this.locationInfo = locationInfo;
		this.level = level;
		this.attributes = attributes;
	}

	public TagInformation(TagInformation tagInfo) {
		super();
		this.document = tagInfo.document;
		this.tagLibrary = tagInfo.tagLibrary;
		this.parent = tagInfo.parent;
		this.tagName = tagInfo.tagName;
		this.locationInfo = tagInfo.locationInfo;
		this.level = tagInfo.level;
		this.attributes = tagInfo.attributes;
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public String getTagName() {
		return tagName;
	}

	public int getLevel() {
		return level;
	}

	@SuppressWarnings("unchecked")
	public <T> T adaptTo(Class<T> type) {
		return (T) (type.isAssignableFrom(getClass()) ? this : null);
	}

	public final <T> T parentAdaptTo(Class<T> type) {
		return (parent != null) ? parent.adaptTo(type) : null;
	}

	public final <T> T parentRecursiveAdaptTo(Class<T> type) {
		T match = parentAdaptTo(type);
		if (match != null) {
			return match;
		}
		if (parent != null) {
			return parent.parentAdaptTo(type);
		}
		return null;
	}

	public ITagLibrary getTagLibrary() {
		return tagLibrary;
	}

	public String getAttribute(String name) {
		return attributes.remove(name);
	}

	public String requireAttribute(String name) {
		String attr = getAttribute(name);
		if (attr == null) {
			throw new TagLibraryException(this, "Required attribute " + name + " not found!");
		}
		return attr;
	}

	public TagNode getParent() {
		return parent;
	}

	public Document getDocument() {
		return document;
	}

}
