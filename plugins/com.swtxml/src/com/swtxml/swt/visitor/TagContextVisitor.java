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

/**
 * A TagContextVisitor proxies another ITagVisitor and establishes the context
 * for the tag before calling visitor.visit().
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class TagContextVisitor implements ITagVisitor {

	private ITagVisitor visitor;

	public TagContextVisitor(ITagVisitor visitor) {
		super();
		this.visitor = visitor;
	}

	public void visit(final Tag tag) {
		Context.runWith(new Runnable() {
			public void run() {
				Context.addAdapter(tag);
				visitor.visit(tag);
			}
		});
	}

}
