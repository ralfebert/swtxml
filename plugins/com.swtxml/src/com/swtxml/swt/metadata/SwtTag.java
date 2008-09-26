package com.swtxml.swt.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.metadata.ITag;
import com.swtxml.metadata.MetaDataException;
import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.reflector.Reflector;

public class SwtTag implements ITag {

	private String className;
	private Class<? extends Widget> swtWidgetClass;

	private Map<String, SwtAttribute> attributes;

	public SwtTag(String className) {
		this.className = className;
	}

	public String getName() {
		return getSwtWidgetClass().getSimpleName();
	}

	public Map<String, SwtAttribute> getAttributes() {
		if (attributes == null) {
			Collection<IReflectorProperty> properties = Reflector
					.findPublicProperties(getSwtWidgetClass());
			attributes = new HashMap<String, SwtAttribute>();
			for (IReflectorProperty prop : properties) {
				SwtAttribute attribute = new SwtAttribute(prop);
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
				this.swtWidgetClass = (Class<? extends Widget>) Class.forName(className);
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
