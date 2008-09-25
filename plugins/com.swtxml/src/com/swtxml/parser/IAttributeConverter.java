/*******************************************************************************
 * Copyright (c) 2008 Ralf Ebert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ralf Ebert - initial API and implementation
 *******************************************************************************/
package com.swtxml.parser;

import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;

public interface IAttributeConverter {

	public static class NotConvertable {
		private final String reason;

		public NotConvertable(final String reason) {
			super();
			this.reason = reason;
		}

		public String getReason() {
			return reason;
		}
	}

	public static final NotConvertable NOT_CONVERTABLE = new NotConvertable("Couldn't convert");

	public Object convert(TagInformation node, TagAttribute attr, Class<?> destClass);

}