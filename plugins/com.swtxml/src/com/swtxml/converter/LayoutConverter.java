package com.swtxml.converter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.util.FormatException;
import com.swtxml.util.KeyValueString;
import com.swtxml.util.ReflectorException;

public class LayoutConverter implements IConverter<Object> {

	private final static String SWT_LAYOUT_PACKAGE = RowLayout.class.getPackage().getName();
	private InjectorDefinition layoutInjector;

	public LayoutConverter(InjectorDefinition layoutInjector) {
		this.layoutInjector = layoutInjector;
	}

	public Object convert(String value) {
		Map<String, String> layoutConstraints = KeyValueString.parse(value);

		String layoutName = layoutConstraints.remove("layout");
		if (layoutName == null) {
			throw new FormatException("no layout specified");
		}

		Layout layout;
		String className = SWT_LAYOUT_PACKAGE + "." + StringUtils.capitalize(layoutName) + "Layout";
		try {
			layout = (Layout) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}

		IInjector injector = layoutInjector.getInjector(layout, true);
		injector.setPropertyValues(layoutConstraints);

		return layout;
	}

}
