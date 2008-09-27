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
import com.swtxml.parser.ITagProcessor;
import com.swtxml.parser.XmlParsingException;
import com.swtxml.util.adapter.IAdaptable;
import com.swtxml.util.parser.ParseException;

public class Tag implements IAdaptable {

	private ITagDefinition tagDefinition;
	private final Tag parent;
	private final String tagName;
	private final String locationInfo;
	private final int level;
	protected final Map<String, String> attributes;
	private final List<Tag> children = new ArrayList<Tag>();
	private final List<Object> adapterObjects;

	public Tag(ITagDefinition tagDefinition, Tag parent, String tagName, String locationInfo,
			int level, Map<String, String> attributes) {
		this.tagDefinition = tagDefinition;
		this.parent = parent;
		this.tagName = tagName;
		this.locationInfo = locationInfo;
		this.level = level;
		this.attributes = attributes;
		this.adapterObjects = new ArrayList<Object>();
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

	public Tag getParent() {
		return parent;
	}

	public List<Tag> getChildren() {
		return children;
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	public Collection<Tag> depthFirst() {
		// TODO: iteration without creating lists?
		List<Tag> contents = new ArrayList<Tag>();
		contents.add(this);
		for (Tag tag : children) {
			contents.addAll(tag.depthFirst());
		}
		return contents;
	}

	public void depthFirst(ITagProcessor... processors) {
		for (Tag tag : depthFirst()) {
			for (ITagProcessor processor : processors) {
				try {
					processor.process(tag);
				} catch (Exception e) {
					throw new XmlParsingException(tag.getLocationInfo() + e.getMessage(), e);
				}
			}
		}
	}

}
