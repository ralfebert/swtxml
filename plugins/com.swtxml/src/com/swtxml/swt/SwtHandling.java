package com.swtxml.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.swt.properties.IIdResolver;
import com.swtxml.swt.properties.setter.ColorConverter;
import com.swtxml.swt.properties.setter.FormAttachmentConverter;
import com.swtxml.swt.properties.setter.LayoutConverter;
import com.swtxml.swt.properties.setter.LayoutDataSetter;
import com.swtxml.swt.properties.setter.PointConverter;
import com.swtxml.swt.properties.setter.StyleConverter;
import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.properties.PropertyMatcher;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.properties.SimpleTypeConverters;

public class SwtHandling {

	public final static ConstantParser SWT = new ConstantParser(SWT.class);

	public static PropertyRegistry createSwtProperties(IIdResolver idResolver) {
		PropertyRegistry layoutInjector = createLayoutProperties(idResolver);

		PropertyRegistry inj = new PropertyRegistry(false);

		inj.add(new PropertyMatcher(Widget.class, "style", Integer.TYPE), new StyleConverter(SWT));

		inj.add(new PropertyMatcher(Composite.class, "layout", Layout.class), new LayoutConverter(
				layoutInjector));

		inj.add(new PropertyMatcher(Color.class), new ColorConverter());

		inj.add(new PropertyMatcher(Point.class), new PointConverter());

		inj.add(new PropertyMatcher(Control.class, "layoutData"), new LayoutDataSetter(
				layoutInjector));

		SimpleTypeConverters.addSimpleTypes(inj);

		return inj;
	}

	public static PropertyRegistry createLayoutProperties(IIdResolver idResolver) {
		PropertyRegistry inj = new PropertyRegistry(true);

		inj.add(new PropertyMatcher(Layout.class, "type", Integer.TYPE), new StyleConverter(SWT
				.filter("HORIZONTAL|VERTICAL")));

		inj.add(new PropertyMatcher(GridData.class, "verticalAlignment", Integer.TYPE),
				new StyleConverter(SWT.filter("BEGINNING|CENTER|END|FILL|TOP|BOTTOM")));

		inj.add(new PropertyMatcher(GridData.class, "horizontalAlignment", Integer.TYPE),
				new StyleConverter(SWT.filter("BEGINNING|CENTER|END|FILL|LEFT|RIGHT")));

		inj.add(new PropertyMatcher(FormAttachment.class), new FormAttachmentConverter(idResolver));

		SimpleTypeConverters.addSimpleTypes(inj);

		return inj;
	}

}
