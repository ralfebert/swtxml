package com.swtxml.swt.injector.setter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Layout;

import com.swtxml.util.injector.IConverter;
import com.swtxml.util.injector.IInjector;
import com.swtxml.util.injector.InjectorDefinition;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.reflector.ReflectorException;

public class LayoutConverter implements IConverter<Object> {

	private final static String SWT_LAYOUT_PACKAGE = RowLayout.class.getPackage().getName();
	private InjectorDefinition layoutInjector;

	public LayoutConverter(InjectorDefinition layoutInjector) {
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

		IInjector injector = layoutInjector.getInjector(layout, true);
		injector.setPropertyValues(layoutConstraints);

		return layout;
	}

}
