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
package com.swtxml.tinydom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.impl.NamespaceDefinition;
import com.swtxml.util.parser.ParseException;

public class Tag implements IAdaptable {

	private final INamespaceDefinition namespaceDefinition;
	private final ITagDefinition tagDefinition;
	private final Tag parent;
	private final String locationInfo;
	protected final Map<INamespaceDefinition, Map<IAttributeDefinition, String>> attributeMap;
	private final List<Tag> children = new ArrayList<Tag>();
	private final List<Object> adapterObjects;

	public Tag(INamespaceDefinition namespaceDefinition, ITagDefinition tagDefinition, Tag parent,
			String locationInfo,
			Map<INamespaceDefinition, Map<IAttributeDefinition, String>> attributeMap) {
		this.namespaceDefinition = namespaceDefinition;
		this.tagDefinition = tagDefinition;
		this.parent = parent;
		this.locationInfo = locationInfo;
		this.attributeMap = attributeMap;
		this.adapterObjects = new ArrayList<Object>();
		if (this.parent != null) {
			this.parent.children.add(this);
		}
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	// TODO: rename to getname
	public String getTagName() {
		return tagDefinition.getName();
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

	public INamespaceDefinition getNamespaceDefinition() {
		return namespaceDefinition;
	}

	public ITagDefinition getTagDefinition() {
		return tagDefinition;
	}

	public String getAttribute(IAttributeDefinition attribute) {
		return getAttribute(getNamespaceDefinition(), attribute);
	}

	public String getAttribute(String attributeName) {
		return getAttribute(getNamespaceDefinition(), getTagDefinition()
				.getAttribute(attributeName));
	}

	public String getAttribute(NamespaceDefinition namespace, String attributeName) {
		if (getNamespaceDefinition().equals(namespace)) {
			return getAttribute(attributeName);
		} else {
			return getAttribute(namespace, namespace.getForeignAttribute(attributeName));
		}
	}

	public Collection<IAttributeDefinition> getAttributes() {
		return getAttributes(getNamespaceDefinition());
	}

	public Collection<IAttributeDefinition> getAttributes(INamespaceDefinition namespace) {
		Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
		if (attributes != null) {
			return attributes.keySet();
		} else {
			return Collections.emptyList();
		}
	}

	public String getAttribute(INamespaceDefinition namespace, IAttributeDefinition attribute) {
		Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
		return attributes != null ? attributes.get(attribute) : null;
	}

	@Deprecated
	public String slurpAttribute(String attributeName) {
		Map<IAttributeDefinition, String> attributes = attributeMap.get(getNamespaceDefinition());
		if (attributes == null) {
			return null;
		}
		IAttributeDefinition attr = tagDefinition.getAttribute(attributeName);
		if (attr == null) {
			return null;
		}
		return attributes.remove(attr);
	}

	public Tag getParent() {
		return parent;
	}

	public List<Tag> getChildren() {
		return children;
	}

	public <A> List<A> adaptChildren(Class<A> type) {
		List<A> results = new ArrayList<A>();
		for (Tag tag : children) {
			A adapted = tag.adaptTo(type);
			if (adapted != null) {
				results.add(adapted);
			}
		}
		return results;
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
					throw new ParseException(tag.getLocationInfo() + e.getMessage(), e);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Tag[" + tagDefinition + "]";
	}

}
