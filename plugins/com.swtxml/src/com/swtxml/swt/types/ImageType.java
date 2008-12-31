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

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.swtxml.resources.IDocumentResource;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.SwtResourceManager;
import com.swtxml.util.context.Context;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class ImageType implements IType<Image>, IContentAssistable {

	private final static HashMap<String, Integer> SWT_ICONS = new HashMap<String, Integer>();

	static {
		for (String constant : SwtInfo.SWT.getConstants()) {
			if (constant.startsWith("ICON_")) {
				SWT_ICONS.put(constant, SwtInfo.SWT.getIntValue(constant));
			}
		}
	}

	public Image convert(String value) {
		if (value.startsWith("ICON_")) {
			Integer constant = SWT_ICONS.get(value.toUpperCase().trim());
			if (constant != null) {
				return Display.getDefault().getSystemImage(constant);
			}

			throw new ParseException("Invalid image value: " + value
					+ " (allowed are constants as defined in SWT.ICON_*)");
		}

		SwtResourceManager resourceManager = Context.adaptTo(SwtResourceManager.class);
		if (resourceManager == null) {
			throw new ParseException("No SWT resource manager available!");
		}
		Map<String, Image> imageRegistry = resourceManager.getImages();

		Image image = imageRegistry.get(value);
		if (image != null) {
			return image;
		}

		IDocumentResource document = Context.adaptTo(IDocumentResource.class);
		if (document == null) {
			throw new ParseException("No resolver available to resolve \"" + value + "\"!");
		}

		InputStream stream = document.resolve(value);
		if (stream == null) {
			throw new ParseException("Resource \"" + value + "\" not found!");
		}

		image = new Image(Display.getDefault(), stream);
		imageRegistry.put(value, image);
		return image;
	}

	public List<Match> getProposals(Match match) {
		return match.propose(SWT_ICONS.keySet());
	}

}