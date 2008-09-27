package com.swtxml.swt.processors;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.swt.IIdResolver;
import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.adapter.IAdaptable;

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
