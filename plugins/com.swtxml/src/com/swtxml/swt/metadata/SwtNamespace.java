package com.swtxml.swt.metadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.swtxml.metadata.INamespace;
import com.swtxml.metadata.MetaDataException;
import com.swtxml.util.lang.IOUtils;

public class SwtNamespace implements INamespace {

	private Map<String, WidgetTag> tagsByName = new HashMap<String, WidgetTag>();

	public SwtNamespace() {
		String widgetClasses = IOUtils.toString(SwtNamespace.class
				.getResourceAsStream("widgets.txt"));
		for (String className : StringUtils.split(widgetClasses)) {
			WidgetTag tag = new WidgetTag(className);
			WidgetTag existingTag = tagsByName.get(tag.getName());
			if (existingTag != null) {
				throw new MetaDataException("Tag naming conflict between " + tag + " and "
						+ existingTag + "!");
			}
			tagsByName.put(tag.getName(), tag);
		}
	}

	public Map<String, WidgetTag> getTags() {
		return tagsByName;
	}

}
