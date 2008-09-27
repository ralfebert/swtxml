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
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.swtxml.metadata.IAttribute;
import com.swtxml.metadata.INamespace;
import com.swtxml.metadata.ITag;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IEnumeratedType;
import com.swtxml.util.types.IType;

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
		if (tag == null) {
			return;
		}

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

	@Override
	protected void addAttributeValueProposals(final ContentAssistRequest contentAssistRequest) {
		ITag tag = registry.getTags().get(contentAssistRequest.getNode().getNodeName());
		if (tag == null) {
			return;
		}

		IDOMNode node = (IDOMNode) contentAssistRequest.getNode();

		// Find the attribute region and name for which this position should
		// have a value proposed
		IStructuredDocumentRegion open = node.getFirstStructuredDocumentRegion();
		ITextRegionList openRegions = open.getRegions();
		int i = openRegions.indexOf(contentAssistRequest.getRegion());
		if (i < 0) {
			return;
		}
		ITextRegion nameRegion = null;
		while (i >= 0) {
			nameRegion = openRegions.get(i--);
			if (nameRegion.getType() == DOMRegionContext.XML_TAG_ATTRIBUTE_NAME) {
				break;
			}
		}

		// the name region is REQUIRED to do anything useful
		if (nameRegion == null) {
			return;
		}

		String attributeName = open.getText(nameRegion);
		IAttribute attribute = tag.getAttributes().get(attributeName);
		if (attribute == null) {
			return;
		}

		IType<?> type = attribute.getType();

		if (type instanceof IEnumeratedType) {
			List<String> proposals = new ArrayList<String>(((IEnumeratedType) type).getEnumValues());
			Collections.sort(proposals);

			Match match = new Match(contentAssistRequest.getText(), contentAssistRequest
					.getMatchString().length());

			// List<String> matchingProposals = new
			// ArrayList<String>(Collections2.filter(proposals,
			// new Predicate<String>() {
			//
			// public boolean apply(String proposal) {
			// return proposal.toLowerCase().startsWith(match);
			// }
			//
			// }));

			// for (String proposal : match) {
			System.out.println(match);
			contentAssistRequest.addProposal(new CompletionProposal(match.getText(),
					contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest
							.getReplacementLength(), match.getCursorPos()));
			// }
		}
	}
}
