package com.swtxml.swt.metadata;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.swtxml.metadata.INamespace;
import com.swtxml.metadata.MetaDataException;

public class SwtNamespace implements INamespace {

	private Map<String, SwtTag> tagsByName = new HashMap<String, SwtTag>();

	public SwtNamespace() {
		try {
			String widgetClasses = IOUtils.toString(SwtNamespace.class
					.getResourceAsStream("widgets.txt"));
			for (String className : StringUtils.split(widgetClasses)) {
				SwtTag tag = new SwtTag(className);
				SwtTag existingTag = tagsByName.get(tag.getName());
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

	public Map<String, SwtTag> getTags() {
		return tagsByName;
	}

}
