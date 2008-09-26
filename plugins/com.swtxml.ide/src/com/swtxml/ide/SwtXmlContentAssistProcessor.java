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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.swtxml.metadata.IAttribute;
import com.swtxml.metadata.INamespace;
import com.swtxml.metadata.ITag;
import com.swtxml.swt.metadata.SwtNamespace;

@SuppressWarnings("restriction")
public class SwtXmlContentAssistProcessor extends XMLContentAssistProcessor {

	// TODO: this should knows nothing about swt
	private INamespace registry = new SwtNamespace();

	public SwtXmlContentAssistProcessor() {
		super();
	}

	@Override
	protected void addTagNameProposals(final ContentAssistRequest contentAssistRequest,
			int childPosition) {
		System.out.println(contentAssistRequest);
		Collection<? extends ITag> tags = registry.getTags().values();
		List<ITag> matchingTags = new ArrayList<ITag>(Collections2.filter(tags,
				new Predicate<ITag>() {

					public boolean apply(ITag tag) {
						return tag.getName().toLowerCase().startsWith(
								contentAssistRequest.getMatchString().toLowerCase());
					}

				}));
		Collections.sort(matchingTags);
		for (ITag tag : matchingTags) {
			contentAssistRequest.addProposal(new CompletionProposal(tag.getName() + "/>",
					contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest
							.getReplacementLength(), 5));
		}
		super.addTagNameProposals(contentAssistRequest, childPosition);
	}

	@Override
	protected void addAttributeNameProposals(final ContentAssistRequest contentAssistRequest) {
		ITag tag = registry.getTags().get(contentAssistRequest.getNode().getNodeName());
		if (tag != null) {
			List<IAttribute> matchingAttributes = new ArrayList<IAttribute>(Collections2.filter(tag
					.getAttributes().values(), new Predicate<IAttribute>() {

				public boolean apply(IAttribute attr) {
					return attr.getName().toLowerCase().startsWith(
							contentAssistRequest.getMatchString().toLowerCase());
				}

			}));
			Collections.sort(matchingAttributes);
			for (IAttribute attr : matchingAttributes) {
				contentAssistRequest.addProposal(new CompletionProposal(attr.getName() + "=\"\"",
						contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest
								.getReplacementLength(), attr.getName().length() + 2));
			}
		}
	}

}
