package com.swtxml.swt.injector;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.SwtConstants;
import com.swtxml.swt.injector.setter.ColorConverter;
import com.swtxml.swt.injector.setter.FormAttachmentConverter;
import com.swtxml.swt.injector.setter.LayoutConverter;
import com.swtxml.swt.injector.setter.LayoutDataSetter;
import com.swtxml.swt.injector.setter.PointConverter;
import com.swtxml.swt.injector.setter.StyleConverter;
import com.swtxml.util.injector.InjectorDefinition;
import com.swtxml.util.injector.PropertyMatcher;
import com.swtxml.util.injector.SimpleTypeConverters;

public class SwtInjectors {

	public static InjectorDefinition createSwtInjector(IIdResolver idResolver) {
		InjectorDefinition layoutInjector = createLayoutInjector(idResolver);

		InjectorDefinition inj = new InjectorDefinition();

		inj.add(new PropertyMatcher(Widget.class, "style", Integer.TYPE), new StyleConverter(
				SwtConstants.SWT));

		inj.add(new PropertyMatcher(Composite.class, "layout", Layout.class), new LayoutConverter(
				layoutInjector));

		inj.add(new PropertyMatcher(Color.class), new ColorConverter());

		inj.add(new PropertyMatcher(Point.class), new PointConverter());

		inj.add(new PropertyMatcher(Control.class, "layoutData"), new LayoutDataSetter(
				layoutInjector));

		SimpleTypeConverters.addSimpleTypes(inj);

		return inj;
	}

	public static InjectorDefinition createLayoutInjector(IIdResolver idResolver) {
		InjectorDefinition inj = new InjectorDefinition();

		inj.add(new PropertyMatcher(Layout.class, "type", Integer.TYPE), new StyleConverter(
				SwtConstants.SWT.filter("HORIZONTAL|VERTICAL")));

		inj
				.add(new PropertyMatcher(GridData.class, "verticalAlignment", Integer.TYPE),
						new StyleConverter(SwtConstants.SWT
								.filter("BEGINNING|CENTER|END|FILL|TOP|BOTTOM")));

		inj
				.add(new PropertyMatcher(GridData.class, "horizontalAlignment", Integer.TYPE),
						new StyleConverter(SwtConstants.SWT
								.filter("BEGINNING|CENTER|END|FILL|LEFT|RIGHT")));

		inj.add(new PropertyMatcher(FormAttachment.class), new FormAttachmentConverter(idResolver));

		SimpleTypeConverters.addSimpleTypes(inj);

		return inj;
	}

}
