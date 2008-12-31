/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.swt.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.SwtResourceManager;
import com.swtxml.util.context.Context;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class ColorType implements IType<Color>, IContentAssistable {

	private final static HashMap<String, Integer> SWT_COLORS = new HashMap<String, Integer>();

	static {
		for (String constant : SwtInfo.SWT.getConstants()) {
			if (constant.startsWith("COLOR_")) {
				SWT_COLORS.put(constant.substring(6), SwtInfo.SWT.getIntValue(constant));
			}
		}
	}

	public Color convert(String value) {
		if (!value.startsWith("#") || value.length() != 7) {
			Integer constant = SWT_COLORS.get(value.toUpperCase().trim());
			if (constant != null) {
				return Display.getDefault().getSystemColor(constant);
			}

			throw new ParseException("Invalid color value: " + value
					+ " (allowed are html colors like #ff00ff or "
					+ "constants like 'red' as defined in SWT.COLOR_RED)");
		}

		SwtResourceManager resourceManager = Context.adaptTo(SwtResourceManager.class);
		if (resourceManager == null) {
			throw new ParseException("No SWT resource manager available!");
		}
		Map<String, Color> colorRegistry = resourceManager.getColors();
		Color color = colorRegistry.get(value);
		if (color != null) {
			return color;
		}

		int i = Integer.parseInt(value.substring(1), 16);
		color = new Color(Display.getDefault(), new RGB((i & 0xff0000) >> 16, (i & 0xff00) >> 8,
				i & 0xff));
		colorRegistry.put(value, color);
		return color;
	}

	public List<Match> getProposals(Match match) {
		return match.propose(SWT_COLORS.keySet());
	}

}
