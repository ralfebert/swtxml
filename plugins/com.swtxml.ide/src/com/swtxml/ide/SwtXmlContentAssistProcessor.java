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
package com.swtxml.ide;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;

@SuppressWarnings("restriction")
public class SwtXmlContentAssistProcessor extends XMLContentAssistProcessor {

	public SwtXmlContentAssistProcessor() {
		super();
	}
	
	@Override
	protected void addTagNameProposals(ContentAssistRequest contentAssistRequest, int childPosition) {
		System.out.println(contentAssistRequest);
		contentAssistRequest.addProposal(new CompletionProposal("hallo/>", contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest.getReplacementLength(), 5));
		super.addTagNameProposals(contentAssistRequest, childPosition);
	}
	
}
