package com.swtxml.swt.processors;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.metadata.WidgetTag;
import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IPredicate;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.reflector.ReflectorException;

public class BuildWidgets implements ITagProcessor {

	private Composite parent;

	public BuildWidgets(Composite parent) {
		this.parent = parent;
	}

	public void process(Tag tag) {
		if (!(tag.getTagDefinition() instanceof WidgetTag)) {
			return;
		}

		if (tag.isRoot()) {
			if (!tag.getTagName().equals(Composite.class.getSimpleName())) {
				throw new ParseException("Invalid root tag " + tag.getTagName() + ", expected <"
						+ Composite.class.getSimpleName() + ">");
			}
			tag.makeAdaptable(parent);
			return;
		}

		WidgetTag widgetTag = (WidgetTag) tag.getTagDefinition();

		Constructor<?> constructor = getWidgetConstructor(widgetTag.getSwtWidgetClass());
		Class<?> parentClass = constructor.getParameterTypes()[0];

		Composite parent = (Composite) tag.parentRecursiveAdaptTo(parentClass);
		Integer style = SwtInfo.SWT.getIntValue(tag.slurpAttribute("style"));

		Widget widget = build(constructor, parent, style == null ? SWT.NONE : style);
		tag.makeAdaptable(widget);
	}

	public Widget build(Constructor<?> constructor, Object parent, int style) {
		try {
			return (Widget) constructor.newInstance(new Object[] { parent, style });
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
