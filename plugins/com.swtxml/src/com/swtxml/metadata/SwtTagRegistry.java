package com.swtxml.metadata;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

public class SwtTagRegistry {

	private Map<String, ITag> tagsByName = new HashMap<String, ITag>();

	public SwtTagRegistry() {
		try {
			String widgetClasses = IOUtils.toString(SwtTagRegistry.class
					.getResourceAsStream("widgets.txt"));
			for (String className : StringUtils.split(widgetClasses)) {
				ITag tag = new SwtTag(className);
				ITag existingTag = tagsByName.get(tag.getName());
				if (existingTag != null) {
					throw new MetaDataException("Tag naming conflict between " + tag + " and "
							+ existingTag + "!");
				}
				tagsByName.put(tag.getName(), tag);
			}
		} catch (IOException e) {
			throw new MetaDataException(e);
		}
	}

	public ITag getTag(String name) {
		return tagsByName.get(name);
	}

	public Collection<ITag> getTags() {
		return tagsByName.values();
	}

}
