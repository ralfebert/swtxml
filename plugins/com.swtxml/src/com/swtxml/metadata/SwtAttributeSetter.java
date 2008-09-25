package com.swtxml.metadata;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.converter.IConverter;
import com.swtxml.converter.SwtConverters;

public class SwtAttributeSetter {

	private SwtTagAttribute attr;

	public SwtAttributeSetter(SwtTagAttribute attr) {
		this.attr = attr;
	}

	// TODO: boolean is for migration purposes
	public boolean set(Widget widget, String value) {
		Class<?> destType = attr.getProperty().getType();
		IConverter<?> converter = SwtConverters.to(destType);
		if (converter == null) {
			converter = SwtConverters.forProperty(attr.getName(), destType);
		}
		if (converter != null) {
			attr.getProperty().set(widget, converter.convert(value));
			return true;
		}
		if ("text".equals(attr.getName())) {
			attr.getProperty().set(widget, value);
			return true;
		}
		return false;
	}

}
