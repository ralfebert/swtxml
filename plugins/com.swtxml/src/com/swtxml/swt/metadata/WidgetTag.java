package com.swtxml.swt.metadata;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.definition.DefinitionException;
import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.impl.AttributeDefinition;
import com.swtxml.swt.SwtHandling;
import com.swtxml.swt.types.StyleType;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.Property;

public class WidgetTag implements ITagDefinition {

	private String className;
	private Class<? extends Widget> swtWidgetClass;

	private Map<String, IAttributeDefinition> attributes;

	public WidgetTag(String className) {
		this.className = className;

		ClassProperties<? extends Widget> properties = SwtHandling.WIDGET_PROPERTIES
				.getProperties(getSwtWidgetClass());
		attributes = new HashMap<String, IAttributeDefinition>();
		for (Property property : properties.getProperties().values()) {
			WidgetAttribute attribute = new WidgetAttribute(property);
			attributes.put(attribute.getName(), attribute);
		}
		attributes.put("style", new AttributeDefinition("style", new StyleType(SwtHandling.SWT)));
	}

	public String getName() {
		return getSwtWidgetClass().getSimpleName();
	}

	@Override
	public String toString() {
		return "SwtTag[" + className + "]";
	}

	@SuppressWarnings("unchecked")
	public Class<? extends Widget> getSwtWidgetClass() {
		if (this.swtWidgetClass == null) {
			try {
				this.swtWidgetClass = (Class<Widget>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new DefinitionException(e);
			}
		}
		return swtWidgetClass;
	}

	public int compareTo(ITagDefinition o) {
		return this.getName().compareTo(o.getName());
	}

	public IAttributeDefinition getAttribute(String name) {
		return attributes.get(name);
	}

	public Set<String> getAttributeNames() {
		return Collections.unmodifiableSet(attributes.keySet());
	}
}
