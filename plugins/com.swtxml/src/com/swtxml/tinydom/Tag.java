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

import org.eclipse.core.runtime.Assert;

import com.swtxml.adapter.IAdaptable;
import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.internal.NamespaceDefinition;
import com.swtxml.util.parser.ParseException;

public final class Tag implements IAdaptable {

	private final INamespaceDefinition namespaceDefinition;
	private final ITagDefinition tagDefinition;
	private final Map<INamespaceDefinition, Map<IAttributeDefinition, String>> attributeMap;

	private final Tag parent;
	private List<Tag> children;

	private final String locationInfo;
	private final List<Object> adapterObjects = new ArrayList<Object>();

	public Tag(INamespaceDefinition namespaceDefinition, ITagDefinition tagDefinition,
			Map<INamespaceDefinition, Map<IAttributeDefinition, String>> attributeMap, Tag parent,
			String locationInfo) {
		this.namespaceDefinition = namespaceDefinition;
		this.tagDefinition = tagDefinition;
		this.parent = parent;
		this.locationInfo = locationInfo;
		this.attributeMap = attributeMap;
		if (!isRoot()) {
			if (this.parent.children == null) {
				this.parent.children = new ArrayList<Tag>();
			}
			this.parent.children.add(this);
		}
	}

	public INamespaceDefinition getNamespaceDefinition() {
		return namespaceDefinition;
	}

	public ITagDefinition getTagDefinition() {
		return tagDefinition;
	}

	public String getName() {
		return tagDefinition.getName();
	}

	public String getLocationInfo() {
		return locationInfo;
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	public Tag getParent() {
		return parent;
	}

	public List<Tag> getChildren() {
		if (children != null) {
			return children;
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Calls the given visitors for this node and its children elements. All
	 * children elements are visited before going up again (Depth first).
	 */
	public void visitDepthFirst(ITagVisitor... visitors) {
		for (ITagVisitor visitor : visitors) {
			try {
				visitor.visit(this);
			} catch (Exception e) {
				throw new ParseException(this.getLocationInfo() + e.getMessage(), e);
			}
		}
		for (Tag child : getChildren()) {
			child.visitDepthFirst(visitors);
		}
	}

	/**
	 * Returns the attribute value by name for attributes having the same
	 * namespace as the tag.
	 */
	public String getAttributeValue(String attributeName) {
		return getAttributeValue(getNamespaceDefinition(), getTagDefinition().getAttribute(
				attributeName));
	}

	/**
	 * Returns the attribute value by namespace and attribute name.
	 */
	public String getAttributeValue(NamespaceDefinition namespace, String attributeName) {
		if (getNamespaceDefinition().equals(namespace)) {
			return getAttributeValue(attributeName);
		} else {
			return getAttributeValue(namespace, namespace.getForeignAttribute(attributeName));
		}
	}

	/**
	 * Returns the attribute value by namespace and attribute definition.
	 */
	public String getAttributeValue(INamespaceDefinition namespace, IAttributeDefinition attribute) {
		Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
		return attributes != null ? attributes.get(attribute) : null;
	}

	/**
	 * Returns a list of all definitions from the given namespace which
	 * specified set for this tag.
	 */
	public Collection<IAttributeDefinition> getAttributes(INamespaceDefinition namespace) {
		Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
		if (attributes != null) {
			return attributes.keySet();
		} else {
			return Collections.emptyList();
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> type) {
		for (Object adapterObject : adapterObjects) {
			if (type.isAssignableFrom(adapterObject.getClass())) {
				return (T) adapterObject;
			}
		}
		return (T) (type.isAssignableFrom(getClass()) ? this : null);
	}

	public void addAdapter(Object adapterObject) {
		Assert.isNotNull(adapterObject, "adapterObject");
		// TODO: check for conflicts
		this.adapterObjects.add(adapterObject);
	}

	public final <T> T getAdapterParent(Class<T> type) {
		return (parent != null) ? parent.getAdapter(type) : null;
	}

	public final <T> T getAdapterParentRecursive(Class<T> type) {
		T match = getAdapterParent(type);
		if (match != null) {
			return match;
		}
		if (parent != null) {
			return parent.getAdapterParent(type);
		}
		return null;
	}

	public <T> List<T> getAdapterChildren(Class<T> type) {
		List<T> results = new ArrayList<T>();
		for (Tag tag : getChildren()) {
			T adapted = tag.getAdapter(type);
			if (adapted != null) {
				results.add(adapted);
			}
		}
		return results;
	}

	@Override
	public String toString() {
		return "Tag[" + tagDefinition + "]";
	}

}
