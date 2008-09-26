package com.swtxml.swt.properties.setter;

import java.util.Map;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.properties.IType;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.reflector.ReflectorException;

public class LayoutDataSetter implements IType<Object> {

	private PropertyRegistry layoutProperties;

	public LayoutDataSetter(PropertyRegistry layoutProperties) {
		this.layoutProperties = layoutProperties;
	}

	public Object convert(Object obj, String value) {
		Control control = (Control) obj;
		Layout layout = control.getParent().getLayout();
		return createLayoutData(layout, value);
	}

	public Object createLayoutData(Layout parentLayout, String value) {
		Map<String, String> layoutConstraints = KeyValueParser.parse(value);

		Class<?> layoutDataClass = getLayoutClass(parentLayout);
		if (layoutDataClass == null) {
			throw new ParseException("Layout " + layoutDataClass.getSimpleName()
					+ " doesn't allow layout data!");
		}

		Object layoutData;
		try {
			layoutData = layoutDataClass.newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
		// TODO: straighten out this "LayoutData" needs to see its parent thing
		// and is a setter for that reason
		layoutProperties.getProperties(layoutData.getClass()).getInjector(layoutData)
				.setPropertyValues(layoutConstraints);
		return layoutData;
	}

	private Class<?> getLayoutClass(Layout layout) {
		if (layout == null) {
			return null;
		}
		if (layout instanceof RowLayout) {
			return RowData.class;
		}
		if (layout instanceof GridLayout) {
			return GridData.class;
		}
		if (layout instanceof FormLayout) {
			return FormData.class;
		}
		return null;
	}

}
