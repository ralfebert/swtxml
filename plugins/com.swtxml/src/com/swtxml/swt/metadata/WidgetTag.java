package com.swtxml.swt.metadata;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.metadata.ITag;
import com.swtxml.metadata.MetaDataException;
import com.swtxml.swt.SwtHandling;
import com.swtxml.swt.properties.IIdResolver;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.Property;
import com.swtxml.util.properties.PropertyRegistry;

public class WidgetTag implements ITag {

	private String className;
	private Class<? extends Widget> swtWidgetClass;

	private Map<String, WidgetAttribute> attributes;

	public WidgetTag(String className) {
		this.className = className;
	}

	public String getName() {
		return getSwtWidgetClass().getSimpleName();
	}

	public Map<String, WidgetAttribute> getAttributes() {
		if (attributes == null) {
			// TODO: id resolving
			PropertyRegistry propertyRegistry = SwtHandling.createSwtProperties(new IIdResolver() {
				public <T> T getById(String id, Class<T> clazz) {
					return null;
				}
			});

			ClassProperties<? extends Widget> properties = propertyRegistry
					.getProperties(getSwtWidgetClass());
			attributes = new HashMap<String, WidgetAttribute>();
			for (Property property : properties.getProperties().values()) {
				WidgetAttribute attribute = new WidgetAttribute(property);
				attributes.put(attribute.getName(), attribute);
			}
		}
		return attributes;
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
				throw new MetaDataException(e);
			}
		}
		return swtWidgetClass;
	}

	public int compareTo(ITag o) {
		return this.getName().compareTo(o.getName());
	}
}
