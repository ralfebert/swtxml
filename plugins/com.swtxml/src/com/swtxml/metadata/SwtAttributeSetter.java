package com.swtxml.metadata;

import org.eclipse.swt.widgets.Widget;

public class SwtAttributeSetter {

	private SwtTagAttribute attr;

	public SwtAttributeSetter(SwtTagAttribute attr) {
		this.attr = attr;
	}

	// TODO: boolean is for migration purposes
	public boolean set(Widget widget, String value) {
		if ("text".equals(attr.getName())) {
			attr.getProperty().set(widget, value);
			return true;
		}
		return false;
	}

}
