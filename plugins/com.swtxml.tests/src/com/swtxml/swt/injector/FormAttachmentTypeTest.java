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
package com.swtxml.swt.injector;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.swtxml.adapter.IIdResolver;
import com.swtxml.adapter.MockAdapter;
import com.swtxml.swt.types.FormAttachmentType;
import com.swtxml.util.context.Context;

public class FormAttachmentTypeTest {

	private IIdResolver idResolver;
	private FormAttachmentType type;
	private Button test;

	@After
	public void cleanupContext() {
		Context.clear();
	}

	@Before
	public void setUp() throws Exception {
		idResolver = createMock(IIdResolver.class);
		Context.addAdapter(new MockAdapter(idResolver));
		test = new Button(new Shell(), SWT.NONE);
		expect(idResolver.getById("test", Control.class)).andReturn(test);
		type = new FormAttachmentType();
		replay(idResolver);
	}

	@Test
	public void testSimple() {
		FormAttachment attachment = type.convert("10");
		assertEquals(10, attachment.offset);
		assertEquals(0, attachment.numerator);
	}

	@Test
	public void testPercentage() {
		FormAttachment attachment = type.convert("90%-10");
		assertEquals(-10, attachment.offset);
		assertEquals(90, attachment.numerator);
	}

	@Test
	public void testReferControl() {
		FormAttachment attachment = type.convert("test-10");
		assertEquals(test, attachment.control);
		assertEquals(-10, attachment.offset);
		assertEquals(0, attachment.numerator);
	}

}
