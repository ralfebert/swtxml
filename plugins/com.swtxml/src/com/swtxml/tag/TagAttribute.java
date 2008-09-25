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
package com.swtxml.tag;

import java.util.List;

import com.swtxml.parser.IAttributeConverter;
import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryXmlParser;
import com.swtxml.parser.IAttributeConverter.NotConvertable;

public class TagAttribute {

	private final TagLibraryXmlParser parser;
	private final ITagLibrary tagLibrary;
	private final String name;
	private final String value;
	private boolean processed = false;
	private final boolean local;

	public TagAttribute(TagLibraryXmlParser parser, ITagLibrary tagLibrary, String name,
			String value, boolean local) {
		this.parser = parser;
		this.tagLibrary = tagLibrary;
		this.name = name;
		this.value = value;
		this.local = local;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed() {
		this.processed = true;
	}

	@Override
	public String toString() {
		return name + "=\"" + value + "\"";
	}

	public String getName() {
		return name;
	}

	public boolean isLocal() {
		return local;
	}

	public ITagLibrary getTagLibrary() {
		return tagLibrary;
	}

	public String getValue() {
		return value;
	}

	public Object getConvertedValue(TagInformation node, Class convertTo) {
		Object convertedValue = IAttributeConverter.NOT_CONVERTABLE;
		if (tagLibrary instanceof IAttributeConverter) {
			convertedValue = ((IAttributeConverter) tagLibrary).convert(node, this, convertTo);
		}
		if (convertedValue instanceof NotConvertable) {
			List<IAttributeConverter> converters = parser.getDefaultConverters();
			for (IAttributeConverter converter : converters) {
				convertedValue = converter.convert(node, this, convertTo);
				if (!(convertedValue instanceof NotConvertable)) {
					break;
				}
			}
		}
		return convertedValue;
	}

	public TagLibraryXmlParser getParser() {
		return parser;
	}

}
