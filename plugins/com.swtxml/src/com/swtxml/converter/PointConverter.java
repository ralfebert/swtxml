package com.swtxml.converter;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.graphics.Point;

public class PointConverter implements IConverter<Point> {

	public Point convert(String value) {
		String[] sizes = StringUtils.split(value, ",x");
		return new Point(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
	}

}
