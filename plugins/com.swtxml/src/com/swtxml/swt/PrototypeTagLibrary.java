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

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;

import com.swtxml.magic.MagicTagLibrary;
import com.swtxml.magic.Parent;
import com.swtxml.magic.Tag;
import com.swtxml.tag.TagInformation;

public class PrototypeTagLibrary extends MagicTagLibrary {

	@Tag
	public ComboViewer list(@Parent Combo combo, TagInformation tag) {
		String[] valueList = StringUtils.split(tag.requireAttribute("values", String.class), '|');
		ComboViewer viewer = new ComboViewer(combo);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(valueList);
		return viewer;
	}

	@Tag
	public ListViewer list(@Parent List list, TagInformation tag) {
		String[] valueList = StringUtils.split(tag.requireAttribute("values", String.class), '|');
		ListViewer viewer = new ListViewer(list);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setInput(valueList);
		return viewer;
	}

	@Tag
	public TablePrototypeData table(@Parent Table table, TagInformation tag) {
		TableViewer viewer = new TableViewer(table);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object arg0, int arg1) {
				return null;
			}

			public String getColumnText(Object obj, int column) {
				String[] data = (String[]) obj;
				return (column <= data.length) ? data[column] : "";
			}

			public void addListener(ILabelProviderListener arg0) {

			}

			public void dispose() {

			}

			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}

			public void removeListener(ILabelProviderListener arg0) {

			}

		});
		return new TablePrototypeData(viewer, tag);
	}

	@Tag
	public void row(@Parent(recursive = true) Table table, @Parent TablePrototypeData data,
			TagInformation tag) {
		data.addRow(StringUtils.split(tag.requireAttribute("values", String.class), '|'));
	}

}
