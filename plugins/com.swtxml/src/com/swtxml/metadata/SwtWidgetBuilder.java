package com.swtxml.metadata;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.eclipse.swt.widgets.Widget;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.swtxml.util.ReflectorException;

public class SwtWidgetBuilder {

	private SwtTag tag;

	public SwtWidgetBuilder(SwtTag tag) {
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
		return Iterables.find(Arrays.asList(widgetClass.getConstructors()),
				new Predicate<Constructor>() {

					public boolean apply(Constructor constructor) {
						return (constructor.getParameterTypes().length == 2 && constructor
								.getParameterTypes()[1] == Integer.TYPE);
					}

				});
	}

}
