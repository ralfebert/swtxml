package com.swtxml.swt.processors;

import com.swtxml.parser.ITagProcessor;
import com.swtxml.swt.metadata.WidgetTag;
import com.swtxml.tag.TagInformation;

public class BuildWidget implements ITagProcessor {

	public void process(TagInformation tag) {
		if (!(tag.getTagDefinition() instanceof WidgetTag)) {
			return;
		}

	}

}
