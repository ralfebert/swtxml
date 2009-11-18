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
import com.swtxml.util.lang.ContractProof;
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

	public String getAttribute(IAttributeDefinition attribute) {
		return getAttribute(getNamespaceDefinition(), attribute);
	}

	/**
	 * Returns the attribute value by name for attributes having the same
	 * namespace as the tag.
	 */
	public String getAttribute(String attributeName) {
		return getAttribute(getNamespaceDefinition(), getTagDefinition()
				.getAttribute(attributeName));
	}

	/**
	 * Returns the attribute value by namespace and attribute name.
	 */
	public String getAttribute(NamespaceDefinition namespace, String attributeName) {
		if (getNamespaceDefinition().equals(namespace)) {
			return getAttribute(attributeName);
		} else {
			return getAttribute(namespace, namespace.getForeignAttribute(attributeName));
		}
	}

	/**
	 * Returns a list of all attribute definitions from the tag's namespace
	 * which are given for this tag.
	 */
	public Collection<IAttributeDefinition> getAttributes() {
		return getAttributes(getNamespaceDefinition());
	}

	/**
	 * Returns a list of all attribute definitions from the given namespace
	 * which are set for this tag.
	 */
	public Collection<IAttributeDefinition> getAttributes(INamespaceDefinition namespace) {
		Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
		if (attributes != null) {
			return attributes.keySet();
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * Returns the attribute value by namespace and attribute attribute
	 * definition.
	 */
	public String getAttribute(INamespaceDefinition namespace, IAttributeDefinition attribute) {
		Map<IAttributeDefinition, String> attributes = attributeMap.get(namespace);
		return attributes != null ? attributes.get(attribute) : null;
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

	public boolean isRoot() {
		return getParent() == null;
	}

	public String getLocationInfo() {
		return locationInfo;
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

	public final <T> T parentAdaptTo(Class<T> type) {
		return (parent != null) ? parent.getAdapter(type) : null;
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

	public void addAdapter(Object adapterObject) {
		ContractProof.notNull(adapterObject, "adapterObject");
		// TODO: check for conflicts
		this.adapterObjects.add(adapterObject);
	}

	/**
	 * Returns a list containing all adapted objects from child nodes which were
	 * adaptable to the given type.
	 */
	public <T> List<T> adaptChildren(Class<T> type) {
		List<T> results = new ArrayList<T>();
		for (Tag tag : getChildren()) {
			T adapted = tag.getAdapter(type);
			if (adapted != null) {
				results.add(adapted);
			}
		}
		return results;
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

	@Override
	public String toString() {
		return "Tag[" + tagDefinition + "]";
	}

}
