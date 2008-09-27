package com.swtxml.swt.metadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.DefinitionException;
import com.swtxml.util.lang.IOUtils;

public class SwtNamespace implements INamespaceDefinition {

	private Map<String, WidgetTag> tagsByName = new HashMap<String, WidgetTag>();

	public SwtNamespace() {
		String widgetClasses = IOUtils.toString(SwtNamespace.class
				.getResourceAsStream("widgets.txt"));
		for (String className : StringUtils.split(widgetClasses)) {
			WidgetTag tag = new WidgetTag(className);
			WidgetTag existingTag = tagsByName.get(tag.getName());
			if (existingTag != null) {
				throw new DefinitionException("Tag naming conflict between " + tag + " and "
						+ existingTag + "!");
			}
			tagsByName.put(tag.getName(), tag);
		}
	}

	public Map<String, WidgetTag> getTags() {
		return tagsByName;
	}

}
