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
package com.swtxml.swt;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

import com.swtxml.converter.SwtConverterLibrary;
import com.swtxml.magic.MagicTagNodeObjectProxy;
import com.swtxml.metadata.ITag;
import com.swtxml.metadata.SwtTagRegistry;
import com.swtxml.metadata.SwtWidgetBuilder;
import com.swtxml.parser.IAttributeConverter;
import com.swtxml.parser.ITagLibrary;
import com.swtxml.parser.TagLibraryException;
import com.swtxml.tag.TagAttribute;
import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;
import com.swtxml.util.KeyValueString;

public class SwtWidgetTagLibrary implements ITagLibrary, IAttributeConverter {

	private SwtTagRegistry registry = new SwtTagRegistry();

	public TagNode tag(TagInformation tagInfo) {

		ITag tag = registry.getTag(tagInfo.getTagName());
		if (tag == null) {
			throw new TagLibraryException(tagInfo, "Unknown tag: " + tagInfo.getTagName());
		}
		SwtWidgetBuilder builder = tag.adaptTo(SwtWidgetBuilder.class);
		if (builder == null) {
			throw new TagLibraryException(tagInfo, "No builder known for " + tag);
		}

		try {
			Integer style = SwtConverterLibrary.getInstance().forProperty("style", Integer.TYPE)
					.convert(tagInfo.getAttribute("style"));
			tagInfo.processAttribute("style");
			Class<?> parentClass = builder.getParentClass();
			Widget widget = builder.build(tagInfo.findParentRecursive(parentClass),
					style == null ? SWT.NONE : style);
			if (widget instanceof TabItem) {
				return new TabItemNode(tag, tagInfo, widget);
			}
			return new MagicTagNodeObjectProxy(tag, tagInfo, widget);
		} catch (Exception e) {
			throw new TagLibraryException(tagInfo, e);
		}

	}

	public Object convert(TagInformation node, TagAttribute attr, Class<?> destClass) {
		if (attr.getName().equals("layoutData")) {
			Map<String, String> layoutConstraints = KeyValueString.parse(attr.getValue());

			Composite composite = node.findParentRecursive(Composite.class);
			Layout layout = composite.getLayout();
			Class<LayoutData> layoutDataClass = getLayoutClass(layout);
			if (layoutDataClass == null) {
				throw new TagLibraryException(node, "Layout " + layout
						+ " doesn't allow layout data!");
			}
			Control control = node.get(Control.class);
			Object layoutData;
			try {
				layoutData = layoutDataClass.newInstance();
			} catch (Exception e) {
				throw new TagLibraryException(node, e);
			}

			for (String name : layoutConstraints.keySet()) {
				String value = layoutConstraints.get(name);
				// TODO: creating a TagAttribute just for injecting it is
				// clearly a HACK - this (and conversion) needs to be refactored
				// out of attributes
				// because we also need it for these css style layout attributes
				TagAttribute fakeAttr = new TagAttribute(attr.getParser(), attr.getTagLibrary(),
						name, value, true);
				SwtHelper.injectAttribute(node, layoutData, fakeAttr, false);
			}

			return layoutData;
		}

		if (destClass == Integer.TYPE
				&& (attr.getName().equals("verticalAlignment") || attr.getName().equals(
						"horizontalAlignment"))) {
			return SwtHelper.requireEnumValue(node, attr.getValue(), SwtHelper.GridDataAlign.class)
					.getSwtConstant();
		}
		if (destClass == Point.class) {
			String[] sizes = StringUtils.split(attr.getValue(), ",x");
			return new Point(Integer.parseInt(sizes[0]), Integer.parseInt(sizes[1]));
		}
		if (destClass == FormAttachment.class) {
			return SwtHelper.parseFormAttachment(node, attr);
		}
		if (destClass == int[].class) {
			String[] intStrings = StringUtils.split(attr.getValue(), ',');
			int[] ints = new int[intStrings.length];
			for (int i = 0; i < ints.length; i++) {
				ints[i] = Integer.parseInt(intStrings[i]);
			}
			return ints;
		}

		return IAttributeConverter.NOT_CONVERTABLE;
	}

	private Class getLayoutClass(Layout layout) {
		if (layout == null) {
			return null;
		}
		if (layout instanceof RowLayout) {
			return RowData.class;
		}
		if (layout instanceof GridLayout) {
			return GridData.class;
		}
		if (layout instanceof FormLayout) {
			return FormData.class;
		}
		return null;
	}

	public void foreignAttribute(TagNode node, TagAttribute attr) {
		// nothing to do
	}

}