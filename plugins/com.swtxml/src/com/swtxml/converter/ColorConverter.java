package com.swtxml.converter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.swtxml.parser.XmlParsingException;
import com.swtxml.util.ReflectorException;

public class ColorConverter implements IConverter<Color> {

	private final static HashMap<String, Integer> SWT_COLORS = new HashMap<String, Integer>();

	static {
		try {
			// TODO: Use reflector API
			Field[] fields = SWT.class.getFields();
			for (Field field : fields) {
				if (field.getName().startsWith("COLOR_") && Modifier.isPublic(field.getModifiers())
						&& Modifier.isStatic(field.getModifiers())
						&& field.getType() == Integer.TYPE) {
					SWT_COLORS.put(field.getName(), field.getInt(SWT.class));
				}
			}
		} catch (Exception e) {
			throw new ReflectorException(e);
		}
	}

	public Color convert(String value) {
		if (!value.startsWith("#") || value.length() != 7) {
			Integer constant = SWT_COLORS.get("COLOR_" + value.toUpperCase().trim());
			if (constant != null) {
				return Display.getDefault().getSystemColor(constant);
			}

			throw new XmlParsingException(
					"Invalid color value: "
							+ value
							+ " (allowed are html colors like #ff00ff or constants like 'red' as defined in SWT.COLOR_RED)");
		}

		// TODO: use color registries
		int i = Integer.parseInt(value.substring(1), 16);
		return new Color(Display.getDefault(), new RGB((i & 0xff0000) >> 16, (i & 0xff00) >> 8,
				i & 0xff));
	}

}
