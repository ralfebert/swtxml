package com.swtxml.converter;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.SwtConstants;

public class Injectors extends InjectorDefinition {

	private final static InjectorDefinition SWT_INJECTOR = createSwtInjector();
	private final static InjectorDefinition LAYOUT_INJECTOR = createLayoutInjector();

	public static InjectorDefinition getSwt() {
		return SWT_INJECTOR;
	}

	public static InjectorDefinition getLayout() {
		return LAYOUT_INJECTOR;
	}

	private static InjectorDefinition createSwtInjector() {
		InjectorDefinition inj = new InjectorDefinition();

		inj.addConverter(new PropertyMatcher(Widget.class, "style", Integer.TYPE),
				new StyleConverter(SwtConstants.SWT));
		inj.addConverter(new PropertyMatcher(Composite.class, "layout", Layout.class),
				new LayoutConverter());
		inj.addConverter(new PropertyMatcher(Color.class), new ColorConverter());
		inj.addSetter(new PropertyMatcher(Control.class, "layoutData"), new LayoutDataSetter());

		SimpleTypeConverters.addSimpleTypes(inj);

		return inj;
	}

	private static InjectorDefinition createLayoutInjector() {
		InjectorDefinition inj = new InjectorDefinition();

		inj.addConverter(new PropertyMatcher(Layout.class, "type", Integer.TYPE),
				new StyleConverter(SwtConstants.SWT.restricted("HORIZONTAL|VERTICAL")));

		inj.addConverter(new PropertyMatcher(GridData.class, "verticalAlignment", Integer.TYPE),
				new StyleConverter(SwtConstants.SWT
						.restricted("BEGINNING|CENTER|END|FILL|TOP|BOTTOM")));

		inj.addConverter(new PropertyMatcher(GridData.class, "horizontalAlignment", Integer.TYPE),
				new StyleConverter(SwtConstants.SWT
						.restricted("BEGINNING|CENTER|END|FILL|LEFT|RIGHT")));

		SimpleTypeConverters.addSimpleTypes(inj);

		return inj;
	}

}
