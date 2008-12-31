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
package com.swtxml.ide;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.swtxml.swt.byid.ById;
import com.swtxml.views.SwtXmlComposite;

/**
 * Composite similar to Eclipse internal class StatusPart for showing Exception
 * messages with message and stack trace.
 * 
 * @author Ralf Ebert <info@ralfebert.de>
 */
public class ErrorComposite extends SwtXmlComposite {

	@ById
	private Text text;
	@ById
	private Button toggleDetails;
	@ById
	private Composite detailsArea;
	@ById
	private Text detailsText;

	public ErrorComposite(Composite parent, int style, Exception e) {
		super(parent, style);
		text.setText(e.getMessage());
		detailsText.setText(ExceptionUtils.getFullStackTrace(e));
	}

	protected void toggleDetails() {
		boolean detailsAreaVisible = !detailsArea.isVisible();
		detailsArea.setVisible(detailsAreaVisible);
		toggleDetails.setText(detailsAreaVisible ? "Hide Details" : "Show Details");
	}
}
