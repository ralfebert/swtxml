package com.swtxml.swt.properties.setter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.properties.IConverter;
import com.swtxml.util.properties.IInjector;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.reflector.ReflectorException;

public class LayoutConverter implements IConverter<Object> {

	private final static String SWT_LAYOUT_PACKAGE = RowLayout.class.getPackage().getName();
	private PropertyRegistry layoutInjector;

	public LayoutConverter(PropertyRegistry layoutInjector) {
		this.layoutInjector = layoutInjector;
	}

	public Object convert(String value) {
		Map<String, String> layoutConstraints = KeyValueParser.parse(value);

		String layoutName = layoutConstraints.remove("layout");
		if (layoutName == null) {
			throw new ParseException("no layout specified");
		}

		Layout layout;
		String className = SWT_LAYOUT_PACKAGE + "." + StringUtils.capitalize(layoutName) + "Layout";
		try {
			layout = (Layout) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}

		IInjector injector = layoutInjector.getInjector(layout);
		injector.setPropertyValues(layoutConstraints);

		return layout;
	}

}