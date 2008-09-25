package com.swtxml.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swtxml.parser.XmlParsingException;

public class Document {

	private Map<String, TagNode> nodesById = new HashMap<String, TagNode>();

	public TagNode getNodeById(String id) {
		return nodesById.get(id);
	}

	public TagNode getRoot() {
		return root;
	}

	private List<TagNode> allNodes = new ArrayList<TagNode>();
	private TagNode root;

	void register(TagNode node) {
		if (node.getParent() == null) {
			if (root != null) {
				throw new XmlParsingException("Root node already set!");
			}
			this.root = node;
		}
		nodesById.put(node.processAttribute("id"), node);
		this.allNodes.add(node);
	}

	@Deprecated
	public Collection<TagNode> getAllNodes() {
		return allNodes;
	}

}
