package com.swtxml.swt.injector.setter;

import java.util.Map;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.util.injector.ISetter;
import com.swtxml.util.injector.InjectorDefinition;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.reflector.IReflectorProperty;
import com.swtxml.util.reflector.ReflectorException;

public class LayoutDataSetter implements ISetter {

	private InjectorDefinition layoutInjector;

	public LayoutDataSetter(InjectorDefinition layoutInjector) {
		this.layoutInjector = layoutInjector;
	}

	public boolean apply(IReflectorProperty bean, Object obj, String name, String value) {
		Control control = (Control) obj;
		Layout layout = control.getParent().getLayout();
		control.setLayoutData(createLayoutData(layout, value));
		return true;
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

		layoutInjector.getInjector(layoutData, true).setPropertyValues(layoutConstraints);
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
