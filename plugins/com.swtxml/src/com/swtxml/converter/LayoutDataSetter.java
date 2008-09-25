package com.swtxml.converter;

import java.util.Map;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.util.FormatException;
import com.swtxml.util.IReflectorProperty;
import com.swtxml.util.KeyValueString;
import com.swtxml.util.ReflectorException;

public class LayoutDataSetter implements ISetter {

	public boolean apply(IReflectorProperty bean, Object obj, String name, String value) {
		Control control = (Control) obj;
		Layout layout = control.getParent().getLayout();
		control.setLayoutData(createLayoutData(layout, value));
		return true;
	}

	public Object createLayoutData(Layout parentLayout, String value) {
		Map<String, String> layoutConstraints = KeyValueString.parse(value);

		Class<?> layoutDataClass = getLayoutClass(parentLayout);
		if (layoutDataClass == null) {
			throw new FormatException("Layout " + layoutDataClass.getSimpleName()
					+ " doesn't allow layout data!");
		}

		Object layoutData;
		try {
			layoutData = layoutDataClass.newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}

		Injectors.getLayout().getInjector(layoutData, true).setPropertyValues(layoutConstraints);
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
