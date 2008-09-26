package com.swtxml.swt.properties.setter;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.graphics.Point;

import com.swtxml.util.properties.IType;

public class PointConverter implements IType<Point> {

	public Point convert(Object obj, String value) {
		String[] sizes = StringUtils.split(value, ",x");
		return new Point(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
	}

}
