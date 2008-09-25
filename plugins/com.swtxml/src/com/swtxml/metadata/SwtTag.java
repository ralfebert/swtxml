package com.swtxml.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.swtxml.util.Reflector;
import com.swtxml.util.ReflectorProperty;

public class SwtTag implements ITag {

	private String className;
	private Class<?> swtWidgetClass;
	private Map<String, ITagAttribute> attributes;

	public SwtTag(String className) {
		this.className = className;
	}

	public String getName() {
		checkClass();
		return swtWidgetClass.getSimpleName();
	}

	private void checkClass() {
		if (this.swtWidgetClass == null) {
			try {
				this.swtWidgetClass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new MetaDataException(e);
			}
		}
	}

	private void checkAttributes() {
		checkClass();
		if (attributes == null) {
			Collection<ReflectorProperty> properties = Reflector
					.findPublicProperties(swtWidgetClass);
			attributes = new HashMap<String, ITagAttribute>();
			for (ReflectorProperty prop : properties) {
				SwtTagAttribute attribute = new SwtTagAttribute(prop);
				attributes.put(attribute.getName(), attribute);
			}
		}
	}

	public ITagAttribute getAttribute(String name) {
		checkAttributes();
		return attributes.get(name);
	}

	public Collection<ITagAttribute> getAttributes() {
		checkAttributes();
		return attributes.values();
	}

	@Override
	public String toString() {
		return "SwtTag[" + className + "]";
	}

	public int compareTo(ITag o) {
		return this.getName().compareTo(o.getName());
	}

}
