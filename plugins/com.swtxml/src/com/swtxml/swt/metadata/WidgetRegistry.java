package com.swtxml.swt.metadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.DefinitionException;
import com.swtxml.util.lang.IOUtils;
import com.swtxml.util.parser.ParseException;

public class WidgetRegistry {

	private Map<String, String[]> widgetStylesByClassName = new HashMap<String, String[]>();

	public WidgetRegistry() {
		String widgetClasses = IOUtils.toString(IOUtils.getClassResource(this.getClass(), "txt"));
		for (String line : StringUtils.split(widgetClasses, "\n")) {
			String[] parts = StringUtils.split(line, '=');
			String className = parts[0].trim();
			String[] allowedStyles = (parts.length > 1) ? StringUtils.split(parts[1].trim(), ", ")
					: new String[] {};
			widgetStylesByClassName.put(className, allowedStyles);
		}
	}

	public Collection<String> getWidgetClassNames() {
		return widgetStylesByClassName.keySet();
	}

	public Collection<String> getAllowedStylesFor(Class<?> widgetClass) {
		Set<String> allowedStyles = new HashSet<String>();
		do {
			String[] styles = widgetStylesByClassName.get(widgetClass.getName());
			if (styles == null) {
				throw new ParseException("Unknown widget super class: " + widgetClass);
			}
			allowedStyles.addAll(Arrays.asList(styles));
			widgetClass = widgetClass.getSuperclass();
		} while (widgetClass.getSuperclass() != null && !Object.class.equals(widgetClass));
		return allowedStyles;
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Widget> getWidgetClass(String className) {
		try {
			return (Class<Widget>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new DefinitionException(e);
		}
	}
}
