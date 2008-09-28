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
import org.eclipse.swt.widgets.Layout;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLContentAssistProcessor;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.swtxml.contracts.IAdaptable;
import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.types.LayoutType;
import com.swtxml.util.context.Context;
import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IPredicate;
import com.swtxml.util.parser.Strictness;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

@SuppressWarnings("restriction")
public class SwtXmlContentAssistProcessor extends XMLContentAssistProcessor {

	public SwtXmlContentAssistProcessor() {
		super();
	}

	// TODO: use match api for tagname / attribute
	@Override
	protected void addTagNameProposals(final ContentAssistRequest contentAssistRequest,
			int childPosition) {

		Match match = createMatch(contentAssistRequest);

		// TODO: this should know nothing about swt
		Collection<String> tags = SwtInfo.NAMESPACE.getTagNames();

		if (contentAssistRequest.getNode() instanceof Text) {
			match = match.insertAroundMatch("", "/>");
		}

		addProposals(contentAssistRequest, match.propose(tags));
		super.addTagNameProposals(contentAssistRequest, childPosition);
	}

	@Override
	protected void addAttributeNameProposals(final ContentAssistRequest contentAssistRequest) {
		ITagDefinition tag = SwtInfo.NAMESPACE.getTag(contentAssistRequest.getNode().getNodeName());
		if (tag == null) {
			return;
		}

		List<String> matchingAttributes = new ArrayList<String>(CollectionUtils.select(tag
				.getAttributeNames(), new IPredicate<String>() {

			public boolean match(String attr) {
				return attr.toLowerCase().startsWith(
						contentAssistRequest.getMatchString().toLowerCase());
			}

		}));
		Collections.sort(matchingAttributes);
		for (String attr : matchingAttributes) {
			contentAssistRequest.addProposal(new CompletionProposal(attr + "=\"\"",
					contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest
							.getReplacementLength(), attr.length() + 2));
		}

	}

	@Override
	protected void addAttributeValueProposals(final ContentAssistRequest contentAssistRequest) {
		ITagDefinition tag = SwtInfo.NAMESPACE.getTag(contentAssistRequest.getNode().getNodeName());
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
		IAttributeDefinition attribute = tag.getAttribute(attributeName);
		if (attribute == null) {
			return;
		}

		// TODO: refactor this out
		if ("layoutData".equals(attributeName)) {
			Context.addAdapter(new IAdaptable() {

				public <A> A adaptTo(Class<A> adapterClass) {
					if (adapterClass == Layout.class) {
						Node layoutNode = contentAssistRequest.getNode().getParentNode()
								.getAttributes().getNamedItem("layout");
						if (layoutNode != null) {
							return (A) new LayoutType().convert(layoutNode.getNodeValue(),
									Strictness.NICE);
						}
					}
					return null;
				}

			});
		}

		IType<?> type = attribute.getType();
		if (type instanceof IContentAssistable) {

			Match match = createMatch(contentAssistRequest).handleQuotes();
			addProposals(contentAssistRequest, ((IContentAssistable) type).getProposals(match));
		}

		Context.clear();
	}

	private void addProposals(final ContentAssistRequest contentAssistRequest, List<Match> proposals) {
		for (Match proposal : proposals) {
			CompletionProposal newProposal = new CompletionProposal(proposal.getReplacementText(),
					contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest
							.getReplacementLength(), proposal.getReplacementCursorPos(), null,
					proposal.getText(), null, null);
			contentAssistRequest.addProposal(newProposal);
		}
	}

	private Match createMatch(final ContentAssistRequest contentAssistRequest) {
		return new Match(contentAssistRequest.getText(), contentAssistRequest.getMatchString()
				.length());
	}
}
