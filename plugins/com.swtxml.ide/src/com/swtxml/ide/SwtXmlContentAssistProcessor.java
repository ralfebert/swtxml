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
import com.swtxml.metadata.ITag;
import com.swtxml.metadata.ITagAttribute;
import com.swtxml.metadata.SwtTagRegistry;

@SuppressWarnings("restriction")
public class SwtXmlContentAssistProcessor extends XMLContentAssistProcessor {

	private SwtTagRegistry registry = new SwtTagRegistry();

	public SwtXmlContentAssistProcessor() {
		super();
	}

	@Override
	protected void addTagNameProposals(final ContentAssistRequest contentAssistRequest,
			int childPosition) {
		System.out.println(contentAssistRequest);
		Collection<ITag> tags = registry.getTags();
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
		ITag tag = registry.getTag(contentAssistRequest.getNode().getNodeName());
		if (tag != null) {
			List<ITagAttribute> matchingAttributes = new ArrayList<ITagAttribute>(Collections2
					.filter(tag.getAttributes(), new Predicate<ITagAttribute>() {

						public boolean apply(ITagAttribute attr) {
							return attr.getName().toLowerCase().startsWith(
									contentAssistRequest.getMatchString().toLowerCase());
						}

					}));
			Collections.sort(matchingAttributes);
			for (ITagAttribute attr : matchingAttributes) {
				contentAssistRequest.addProposal(new CompletionProposal(attr.getName() + "=\"\"",
						contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest
								.getReplacementLength(), attr.getName().length() + 2));
			}
		}
	}

}
