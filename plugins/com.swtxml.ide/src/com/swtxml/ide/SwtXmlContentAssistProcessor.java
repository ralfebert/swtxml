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

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.definition.ITagDefinition;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.swt.types.LayoutType;
import com.swtxml.util.adapter.IAdaptable;
import com.swtxml.util.context.Context;
import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IPredicate;
import com.swtxml.util.parser.Strictness;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

@SuppressWarnings("restriction")
public class SwtXmlContentAssistProcessor extends XMLContentAssistProcessor {

	// TODO: this should know nothing about swt
	private INamespaceDefinition registry = new SwtNamespace();

	public SwtXmlContentAssistProcessor() {
		super();
	}

	// TODO: use match api for tagname / attribute
	@Override
	protected void addTagNameProposals(final ContentAssistRequest contentAssistRequest,
			int childPosition) {
		Collection<String> tags = registry.getTagNames();
		List<String> matchingTags = new ArrayList<String>(CollectionUtils.select(tags,
				new IPredicate<String>() {

					public boolean match(String tag) {
						return tag.toLowerCase().startsWith(
								contentAssistRequest.getMatchString().toLowerCase());
					}

				}));
		Collections.sort(matchingTags);
		for (String tag : matchingTags) {
			contentAssistRequest.addProposal(new CompletionProposal(tag + "/>",
					contentAssistRequest.getReplacementBeginPosition(), contentAssistRequest
							.getReplacementLength(), 5));
		}
		super.addTagNameProposals(contentAssistRequest, childPosition);
	}

	@Override
	protected void addAttributeNameProposals(final ContentAssistRequest contentAssistRequest) {
		ITagDefinition tag = registry.getTag(contentAssistRequest.getNode().getNodeName());
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
		ITagDefinition tag = registry.getTag(contentAssistRequest.getNode().getNodeName());
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

			Match match = new Match(contentAssistRequest.getText(), contentAssistRequest
					.getMatchString().length()).handleQuotes();
			List<Match> proposals = ((IContentAssistable) type).getProposals(match);

			for (Match proposal : proposals) {
				CompletionProposal newProposal = new CompletionProposal(proposal
						.getReplacementText(), contentAssistRequest.getReplacementBeginPosition(),
						contentAssistRequest.getReplacementLength(), proposal
								.getReplacementCursorPos(), null, proposal.getText(), null, null);
				contentAssistRequest.addProposal(newProposal);
			}
		}

		Context.clear();
	}
}
