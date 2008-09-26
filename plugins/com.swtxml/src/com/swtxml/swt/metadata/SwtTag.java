package com.swtxml.swt.metadata;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.metadata.IAttribute;
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
		checkClass();
		return swtWidgetClass.getSimpleName();
	}

	@SuppressWarnings("unchecked")
	private void checkClass() {
		if (this.swtWidgetClass == null) {
			try {
				this.swtWidgetClass = (Class<? extends Widget>) Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new MetaDataException(e);
			}
		}
	}

	private void checkAttributes() {
		checkClass();
		if (attributes == null) {
			Collection<IReflectorProperty> properties = Reflector
					.findPublicProperties(swtWidgetClass);
			attributes = new HashMap<String, SwtAttribute>();
			for (IReflectorProperty prop : properties) {
				SwtAttribute attribute = new SwtAttribute(prop);
				attributes.put(attribute.getName(), attribute);
			}
		}
	}

	public IAttribute getAttribute(String name) {
		checkAttributes();
		return attributes.get(name);
	}

	public Map<String, SwtAttribute> getAttributes() {
		checkAttributes();
		return attributes;
	}

	@Override
	public String toString() {
		return "SwtTag[" + className + "]";
	}

	@SuppressWarnings("unchecked")
	public <T> T adaptTo(Class<T> clazz) {
		if (clazz.isAssignableFrom(SwtWidgetBuilder.class)) {
			return (T) new SwtWidgetBuilder(this);
		}
		return null;
	}

	public Class<? extends Widget> getSwtWidgetClass() {
		return swtWidgetClass;
	}

	public int compareTo(ITag o) {
		return this.getName().compareTo(o.getName());
	}
}
