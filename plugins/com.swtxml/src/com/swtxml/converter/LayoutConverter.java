package com.swtxml.converter;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.RowLayout;

import com.swtxml.util.FormatException;
import com.swtxml.util.KeyValueString;
import com.swtxml.util.ReflectorException;

public class LayoutConverter implements IConverter<Object> {

	private final static String SWT_LAYOUT_PACKAGE = RowLayout.class.getPackage().getName();

	public Object convert(String value) {
		Map<String, String> layoutConstraints = KeyValueString.parse(value);

		// Composite composite = node.get(Composite.class);
		String layoutName = layoutConstraints.remove("layout");
		Object layout;
		if (layoutName == null) {
			throw new FormatException("no layout specified");
		}

		try {
			layout = Class.forName(
					SWT_LAYOUT_PACKAGE + "." + StringUtils.capitalize(layoutName) + "Layout")
					.newInstance();
		} catch (Exception e) {
			throw new ReflectorException(e);
		}

		ConvertingInjector injector = new ConvertingInjector(layout, SwtConverterLibrary
				.getInstance(), true);
		for (String name : layoutConstraints.keySet()) {
			injector.setPropertyValue(name, layoutConstraints.get(name));
		}

		return layout;
	}

}
