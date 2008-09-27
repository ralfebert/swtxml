package com.swtxml.tag;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.parser.XmlParsingException;
import com.swtxml.swt.properties.IIdResolver;
import com.swtxml.util.adapter.IAdaptable;

public class Document implements IIdResolver, IAdaptable {

	private Map<String, TagInformation> nodesById = new HashMap<String, TagInformation>();

	public <T> T getById(String id, Class<T> clazz) {
		TagInformation node = nodesById.get(id);
		if (node == null) {
			return null;
		}

		return node.adaptTo(clazz);
	}

	public TagInformation getRoot() {
		return root;
	}

	private TagInformation root;

	void register(TagInformation node) {
		if (node.getParent() == null) {
			if (root != null) {
				throw new XmlParsingException("Root node already set!");
			}
			this.root = node;
		}
		// TODO: refactor out into processor, maybe recursive processors for
		// setting context?
		String id = node.slurpAttribute("id");
		if (id != null) {
			nodesById.put(id, node);
		}
	}

	@SuppressWarnings("unchecked")
	public <A> A adaptTo(Class<A> adapterClass) {
		if (IIdResolver.class.equals(adapterClass)) {
			return (A) this;
		}
		return null;
	}

}
