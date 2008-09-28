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
package com.swtxml.swt.processors;

import com.swtxml.tinydom.ITagProcessor;
import com.swtxml.tinydom.Tag;
import com.swtxml.util.context.Context;

public class TagContextProcessor implements ITagProcessor {

	private ITagProcessor processor;

	public TagContextProcessor(ITagProcessor processor) {
		super();
		this.processor = processor;
	}

	public void process(final Tag tag) {
		Context.runWith(new Runnable() {
			public void run() {
				Context.addAdapter(tag);
				processor.process(tag);
			}
		});
	}

}
