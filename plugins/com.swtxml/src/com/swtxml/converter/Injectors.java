package com.swtxml.converter;

import static com.swtxml.converter.PropertyMatcher.ALL_CLASSES;
import static com.swtxml.converter.PropertyMatcher.ALL_PROPERTIES;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

public class Injectors extends InjectorDefinition {

	private final static InjectorDefinition SWT_INJECTOR = createSwtInjector();

	public static InjectorDefinition getSwt() {
		return SWT_INJECTOR;
	}

	private static InjectorDefinition createSwtInjector() {
		InjectorDefinition inj = new InjectorDefinition();

		inj.addConverter(new StyleConverter(), Widget.class, "style", Integer.TYPE);
		inj.addConverter(new StyleConverter(), Layout.class, "type", Integer.TYPE);
		inj.addConverter(new LayoutConverter(), Composite.class, "layout", Layout.class);
		inj.addConverter(new ColorConverter(), ALL_CLASSES, ALL_PROPERTIES, Color.class);
		SimpleTypeConverters.addSimpleTypes(inj);

		return inj;
	}

}
