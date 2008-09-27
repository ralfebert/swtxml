package com.swtxml.swt.metadata;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IPredicate;
import com.swtxml.util.reflector.ReflectorException;

public class WidgetBuilder {

	private WidgetTag tag;

	public WidgetBuilder(WidgetTag tag) {
		this.tag = tag;
	}

	public Class<?> getParentClass() {
		Constructor<?> constructor = getWidgetConstructor(tag.getSwtWidgetClass());
		return constructor.getParameterTypes()[0];
	}

	@SuppressWarnings("unchecked")
	public Widget build(Object parent, int style) {
		Class<? extends Widget> widgetClass = tag.getSwtWidgetClass();
		Constructor widgetConstructor = getWidgetConstructor(widgetClass);
		try {
			return (Widget) widgetConstructor.newInstance(new Object[] { parent, style });
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Constructor getWidgetConstructor(Class<? extends Widget> widgetClass) {
		return CollectionUtils.find(Arrays.asList(widgetClass.getConstructors()),
				new IPredicate<Constructor>() {

					public boolean match(Constructor constructor) {
						return (constructor.getParameterTypes().length == 2 && constructor
								.getParameterTypes()[1] == Integer.TYPE);
					}

				});
	}

}
