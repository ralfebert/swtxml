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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Control;

import com.swtxml.adapter.IIdResolver;
import com.swtxml.swt.SwtInfo;
import com.swtxml.util.context.Context;
import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.types.IType;

public class FormAttachmentType implements IType<FormAttachment> {

	private final static ConstantParser CONSTANTS_ALIGN = SwtInfo.SWT
			.filter("TOP|BOTTOM|LEFT|RIGHT|CENTER|DEFAULT");

	public FormAttachmentType() {
	}

	public FormAttachment convert(String value) {
		FormAttachment attachment = new FormAttachment();
		List<String> parts = new ArrayList<String>();
		int start = 0;
		for (int i = 0; i < value.length(); i++) {
			char charAt = value.charAt(i);
			if (charAt == '-' || charAt == '+') {
				parts.add(value.substring(start, i).trim());
				start = i;
			}
		}
		parts.add(value.substring(start, value.length()).trim());

		for (String part : parts) {
			if (part.endsWith("%")) {
				if (part.startsWith("+")) {
					part = part.substring(1);
				}
				attachment.numerator = Integer.parseInt(part.substring(0, part.length() - 1));
			} else {
				try {
					if (part.startsWith("+")) {
						part = part.substring(1);
					}
					attachment.offset = Integer.parseInt(part);
				} catch (NumberFormatException e) {
					String[] controlString = StringUtils.split(part, ".");
					if (controlString.length >= 1) {
						String id = controlString[0].trim();
						IIdResolver idResolver = Context.adaptTo(IIdResolver.class);
						if (idResolver == null) {
							throw new ParseException("No IDs available in current context");
						}
						Control control = idResolver.getById(id, Control.class);
						if (control == null) {
							throw new ParseException("Control with id \"" + id + "\" not found");
						}
						attachment.control = control;
					}
					if (controlString.length >= 2) {
						String align = controlString[1].trim();
						attachment.alignment = CONSTANTS_ALIGN.getIntValue(align);
					}
				}
			}
		}
		return attachment;
	}

}
