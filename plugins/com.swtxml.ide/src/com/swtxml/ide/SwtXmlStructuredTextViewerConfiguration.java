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

import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.core.text.IStructuredPartitions;
import org.eclipse.wst.xml.core.text.IXMLPartitions;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;

public class SwtXmlStructuredTextViewerConfiguration extends StructuredTextViewerConfigurationXML {
	@Override
	public IContentAssistProcessor[] getContentAssistProcessors(
			ISourceViewer sourceViewer, String partitionType) {

		IContentAssistProcessor[] processors;

		if (partitionType == IStructuredPartitions.DEFAULT_PARTITION
				|| partitionType == IXMLPartitions.XML_DEFAULT) {
			processors = new IContentAssistProcessor[] { new SwtXmlContentAssistProcessor() };
		}
		else {
			processors = super.getContentAssistProcessors(sourceViewer,
					partitionType);
		}
		return processors;
	}

}
