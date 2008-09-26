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
import com.swtxml.swt.types.ColorType;
import com.swtxml.swt.types.FormAttachmentType;
import com.swtxml.swt.types.LayoutDataType;
import com.swtxml.swt.types.LayoutType;
import com.swtxml.swt.types.PointType;
import com.swtxml.swt.types.StyleType;
import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.properties.PropertyMatcher;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.types.SimpleTypes;

public class SwtHandling {

	public final static ConstantParser SWT = new ConstantParser(SWT.class);

	public static PropertyRegistry createSwtProperties(IIdResolver idResolver) {
		PropertyRegistry layoutInjector = createLayoutProperties(idResolver);

		PropertyRegistry inj = new PropertyRegistry(false);

		inj.add(new PropertyMatcher(Widget.class, "style", Integer.TYPE), new StyleType(SWT));

		inj.add(new PropertyMatcher(Composite.class, "layout", Layout.class), new LayoutType(
				layoutInjector));

		inj.add(new PropertyMatcher(Color.class), new ColorType());

		inj.add(new PropertyMatcher(Point.class), new PointType());

		inj.add(new PropertyMatcher(Control.class, "layoutData"), new LayoutDataType(
				layoutInjector));

		SimpleTypes.addSimpleTypes(inj);

		return inj;
	}

	public static PropertyRegistry createLayoutProperties(IIdResolver idResolver) {
		PropertyRegistry inj = new PropertyRegistry(true);

		inj.add(new PropertyMatcher(Layout.class, "type", Integer.TYPE), new StyleType(SWT
				.filter("HORIZONTAL|VERTICAL")));

		inj.add(new PropertyMatcher(GridData.class, "verticalAlignment", Integer.TYPE),
				new StyleType(SWT.filter("BEGINNING|CENTER|END|FILL|TOP|BOTTOM")));

		inj.add(new PropertyMatcher(GridData.class, "horizontalAlignment", Integer.TYPE),
				new StyleType(SWT.filter("BEGINNING|CENTER|END|FILL|LEFT|RIGHT")));

		inj.add(new PropertyMatcher(FormAttachment.class), new FormAttachmentType(idResolver));

		SimpleTypes.addSimpleTypes(inj);

		return inj;
	}

}
