package com.swtxml.swt.types;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.graphics.Point;

import com.swtxml.util.types.IType;

public class PointType implements IType<Point> {

	public Point convert(String value) {
		String[] sizes = StringUtils.split(value, ",x");
		return new Point(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
	}

}
