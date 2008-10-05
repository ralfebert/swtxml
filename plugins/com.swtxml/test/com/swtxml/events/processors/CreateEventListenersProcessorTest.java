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
package com.swtxml.events.processors;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import com.swtxml.events.visitor.CreateEventListeners;

public class CreateEventListenersProcessorTest {

	public static interface ISomeView {

		public void testKeyPressed();

		public void testFocusGained(FocusEvent e);

		public void testMouseScrolled(MouseEvent e);

		public void testWidgetSelected();

	}

	@Test
	public void testWireViewMethodListener() {
		ISomeView view = createStrictMock(ISomeView.class);
		CreateEventListeners visitor = new CreateEventListeners(view);
		Shell shell = new Shell();
		Button btn = new Button(shell, SWT.NONE);
		visitor.wireViewMethodListener("testKeyPressed", btn, "keyPressed");
		visitor.wireViewMethodListener("testFocusGained", btn, "focusGained");
		visitor.wireViewMethodListener("testMouseScrolled", btn, "mouseScrolled");
		visitor.wireViewMethodListener("testWidgetSelected", btn, "widgetSelected");

		view.testKeyPressed();
		view.testFocusGained((FocusEvent) anyObject());
		view.testMouseScrolled((MouseEvent) anyObject());
		view.testWidgetSelected();

		replay(view);

		btn.notifyListeners(SWT.KeyDown, new Event());
		btn.notifyListeners(SWT.KeyUp, new Event());
		btn.notifyListeners(SWT.FocusIn, new Event());
		btn.notifyListeners(SWT.MouseWheel, new Event());
		btn.notifyListeners(SWT.Selection, new Event());

		verify(view);
	}

	@Test
	public void testViewExceptionMethod() {
		ISomeView view = createStrictMock(ISomeView.class);
		CreateEventListeners processor = new CreateEventListeners(view);
		Shell shell = new Shell();
		Button btn = new Button(shell, SWT.NONE);
		processor.wireViewMethodListener("testKeyPressed", btn, "keyPressed");

		view.testKeyPressed();
		expectLastCall().andThrow(new NullPointerException());

		replay(view);

		try {
			btn.notifyListeners(SWT.KeyDown, new Event());
			fail("expected exception");
		} catch (Exception e) {
		}

		verify(view);
	}
}
