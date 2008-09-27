package com.swtxml.tag;

import com.swtxml.parser.XmlParsingException;

//TODO: remove document
public class Document {

	public Tag getRoot() {
		return root;
	}

	private Tag root;

	void register(Tag node) {
		if (node.getParent() == null) {
			if (root != null) {
				throw new XmlParsingException("Root node already set!");
			}
			this.root = node;
		}
	}

}
