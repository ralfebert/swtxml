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

import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.TableViewer;

import com.swtxml.tag.TagInformation;
import com.swtxml.tag.TagNode;

public class TablePrototypeData extends TagNode {

	private List<String[]> rows = new Vector<String[]>();
	private TableViewer viewer;

	public TablePrototypeData(TableViewer viewer, TagInformation tagInfo) {
		super(tagInfo);
		this.viewer = viewer;
	}

	@Override
	public <T> T adaptTo(Class<T> type) {
		if (TableViewer.class.isAssignableFrom(type)) {
			return (T) viewer;
		} else {
			return super.adaptTo(type);
		}
	}

	public void addRow(String[] values) {
		rows.add(values);
		viewer.refresh();
	}

	@Override
	public void process() {
		super.process();
		viewer.setInput(rows);
	}

}
