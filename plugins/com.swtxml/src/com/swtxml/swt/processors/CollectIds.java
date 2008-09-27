package com.swtxml.swt.processors;

import java.util.HashMap;
import java.util.Map;

import com.swtxml.parser.ITagProcessor;
import com.swtxml.tag.Tag;

public class CollectIds implements ITagProcessor {

	private Map<String, Tag> tagsById = new HashMap<String, Tag>();

	public void process(Tag tag) {
		String id = tag.slurpAttribute("id");
		if (id != null) {
			tagsById.put(id, tag);
		}
	}

}
