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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.swtxml.definition.ITagDefinition;
import com.swtxml.util.adapter.IAdaptable;
import com.swtxml.util.parser.ParseException;

public class TagInformation implements IAdaptable {

	private final Document document;

	private ITagDefinition tagDefinition;

	private final TagInformation parent;
	private final String tagName;
	private final String locationInfo;
	private final int level;
	protected final Map<String, String> attributes;
	private final List<TagInformation> children = new ArrayList<TagInformation>();
	private final List<Object> adapterObjects;

	public TagInformation(Document document, ITagDefinition tagDefinition, TagInformation parent,
			String tagName, String locationInfo, int level, Map<String, String> attributes) {
		this.document = document;
		this.tagDefinition = tagDefinition;
		this.parent = parent;
		this.tagName = tagName;
		this.locationInfo = locationInfo;
		this.level = level;
		this.attributes = attributes;
		this.adapterObjects = new ArrayList<Object>();
		this.getDocument().register(this);
		if (this.parent != null) {
			this.parent.children.add(this);
		}
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
		for (Object adapterObject : adapterObjects) {
			if (type.isAssignableFrom(adapterObject.getClass())) {
				return (T) adapterObject;
			}
		}
		return (T) (type.isAssignableFrom(getClass()) ? this : null);
	}

	public void makeAdaptable(Object obj) {
		if (obj == null) {
			throw new ParseException("makeAdaptable may not be called with null");
		}
		// TODO: check for conflicts
		this.adapterObjects.add(obj);
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

	public ITagDefinition getTagDefinition() {
		return tagDefinition;
	}

	public String getAttribute(String name) {
		return attributes.get(name);
	}

	public String slurpAttribute(String name) {
		return attributes.remove(name);
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public TagInformation getParent() {
		return parent;
	}

	public Document getDocument() {
		return document;
	}

	public List<TagInformation> getChildren() {
		return children;
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	public Collection<TagInformation> depthFirst() {
		// TODO: iteration without creating lists?
		List<TagInformation> contents = new ArrayList<TagInformation>();
		contents.add(this);
		for (TagInformation tag : children) {
			contents.addAll(tag.depthFirst());
		}
		return contents;
	}

}
