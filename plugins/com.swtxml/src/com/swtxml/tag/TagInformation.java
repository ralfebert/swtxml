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

import java.util.List;

import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.parser.IAttributeConverter.NotConvertable;

public class TagInformation {

	private final Document document;

	private final ITagLibrary tagLibrary;
	private final TagNode parent;
	private final String tagName;
	private final String locationInfo;
	private final int level;
	private final List<TagAttribute> attributes;

	public TagInformation(Document document, ITagLibrary tagLibrary, TagNode parent,
			String tagName, String locationInfo, int level, List<TagAttribute> attributes) {
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

	public <T> T get(Class<T> type) {
		return (T) (type.isAssignableFrom(getClass()) ? this : null);
	}

	public final <T> T findParent(Class<T> type) {
		return (parent != null) ? parent.get(type) : null;
	}

	public final <T> T findParentRecursive(Class<T> type) {
		T match = findParent(type);
		if (match != null) {
			return match;
		}
		if (parent != null) {
			return parent.findParent(type);
		}
		return null;
	}

	public ITagLibrary getTagLibrary() {
		return tagLibrary;
	}

	protected List<TagAttribute> getAttributes() {
		return attributes;
	}

	public String getAttribute(String name) {
		for (TagAttribute tagAttribute : attributes) {
			if (tagAttribute.isLocal() && tagAttribute.getName().equals(name)) {
				return tagAttribute.getValue();
			}
		}
		return null;
	}

	public String processAttribute(String name) {
		for (TagAttribute tagAttribute : attributes) {
			if (tagAttribute.isLocal() && tagAttribute.getName().equals(name)) {
				tagAttribute.setProcessed();
				return tagAttribute.getValue();
			}
		}
		return null;
	}

	public <T> T getConvertedAttribute(String name, Class<T> convertTo) {
		for (TagAttribute tagAttribute : attributes) {
			if (tagAttribute.isLocal() && tagAttribute.getName().equals(name)) {
				// TODO: getting the converted attribute marks it as processed -
				// this is awkward
				tagAttribute.setProcessed();
				T convertedValue = (T) tagAttribute.getConvertedValue(this, convertTo);
				if (convertedValue instanceof NotConvertable) {
					throw new TagLibraryException(this, "Attribute " + tagAttribute
							+ " couldn't be converted to " + convertTo);
				}
				return convertedValue;
			}
		}
		return null;
	}

	public <T> T requireAttribute(String name, Class<T> convertTo) {
		T attr = getConvertedAttribute(name, convertTo);
		if (attr != null) {
			return attr;
		}

		throw new TagLibraryException(this, "Required attribute " + name + " not found!");
	}

	public TagNode getParent() {
		return parent;
	}

	public Document getDocument() {
		return document;
	}

}
