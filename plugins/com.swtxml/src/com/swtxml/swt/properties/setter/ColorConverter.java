package com.swtxml.swt.properties.setter;

import java.util.HashMap;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.swtxml.parser.XmlParsingException;
import com.swtxml.swt.SwtHandling;
import com.swtxml.util.properties.IType;

public class ColorConverter implements IType<Color> {

	private final static HashMap<String, Integer> SWT_COLORS = new HashMap<String, Integer>();

	static {
		for (String constant : SwtHandling.SWT.getConstants()) {
			if (constant.startsWith("COLOR_")) {
				SWT_COLORS.put(constant.substring(6), SwtHandling.SWT.getIntValue(constant));
			}
		}
	}

	public Color convert(Object obj, String value) {
		if (!value.startsWith("#") || value.length() != 7) {
			Integer constant = SWT_COLORS.get(value.toUpperCase().trim());
			if (constant != null) {
				return Display.getDefault().getSystemColor(constant);
			}

			throw new XmlParsingException("Invalid color value: " + value
					+ " (allowed are html colors like #ff00ff or "
					+ "constants like 'red' as defined in SWT.COLOR_RED)");
		}

		// TODO: use color registries
		int i = Integer.parseInt(value.substring(1), 16);
		return new Color(Display.getDefault(), new RGB((i & 0xff0000) >> 16, (i & 0xff00) >> 8,
				i & 0xff));
	}

}
