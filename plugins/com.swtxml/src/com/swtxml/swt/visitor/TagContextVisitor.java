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
package com.swtxml.swt.visitor;

import com.swtxml.tinydom.ITagVisitor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.context.Context;

public class TagContextVisitor implements ITagVisitor {

	private ITagVisitor processor;

	public TagContextVisitor(ITagVisitor processor) {
		super();
		this.processor = processor;
	}

	public void visit(final Tag tag) {
		Context.runWith(new Runnable() {
			public void run() {
				Context.addAdapter(tag);
				processor.visit(tag);
			}
		});
	}

}
