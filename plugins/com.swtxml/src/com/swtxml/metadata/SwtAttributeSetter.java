package com.swtxml.metadata;

import org.eclipse.swt.widgets.Widget;

import com.swtxml.converter.ConvertingInjector;
import com.swtxml.converter.SwtConverterLibrary;

public class SwtAttributeSetter {

	private SwtTagAttribute attr;

	public SwtAttributeSetter(SwtTagAttribute attr) {
		this.attr = attr;
	}

	// TODO: boolean is for migration purposes
	public boolean set(Widget widget, String value) {
		try {
			new ConvertingInjector(widget, SwtConverterLibrary.getInstance(), false)
					.setPropertyValue(attr.getName(), value);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

}
