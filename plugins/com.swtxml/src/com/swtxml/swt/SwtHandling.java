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

	public final static PropertyRegistry WIDGET_PROPERTIES = createWidgetProperties();

	public final static PropertyRegistry LAYOUT_PROPERTIES = createLayoutProperties();

	private static PropertyRegistry createWidgetProperties() {
		PropertyRegistry props = new PropertyRegistry(false);
		props.add(new PropertyMatcher(Widget.class, "style", Integer.TYPE), new StyleType(SWT));
		props.add(new PropertyMatcher(Composite.class, "layout", Layout.class), new LayoutType());
		props.add(new PropertyMatcher(Color.class), new ColorType());
		props.add(new PropertyMatcher(Point.class), new PointType());
		props.add(new PropertyMatcher(Control.class, "layoutData"), new LayoutDataType());
		SimpleTypes.addSimpleTypes(props);
		return props;
	}

	private static PropertyRegistry createLayoutProperties() {
		PropertyRegistry props = new PropertyRegistry(true);

		props.add(new PropertyMatcher(Layout.class, "type", Integer.TYPE), new StyleType(SWT
				.filter("HORIZONTAL|VERTICAL")));

		props.add(new PropertyMatcher(GridData.class, "verticalAlignment", Integer.TYPE),
				new StyleType(SWT.filter("BEGINNING|CENTER|END|FILL|TOP|BOTTOM")));

		props.add(new PropertyMatcher(GridData.class, "horizontalAlignment", Integer.TYPE),
				new StyleType(SWT.filter("BEGINNING|CENTER|END|FILL|LEFT|RIGHT")));

		props.add(new PropertyMatcher(FormAttachment.class), new FormAttachmentType());

		SimpleTypes.addSimpleTypes(props);

		return props;
	}

}
