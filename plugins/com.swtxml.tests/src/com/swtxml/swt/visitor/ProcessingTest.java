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
package com.swtxml.swt.visitor;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;

import org.easymock.EasyMock;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.definition.internal.ForeignAttributeDefinition;
import com.swtxml.definition.internal.NamespaceDefinition;
import com.swtxml.definition.internal.TagDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.SwtXmlParser;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.util.types.SimpleTypes;

public class ProcessingTest {

	@Test
	public void testProcessorsNotDistractedByForeignTagsAndAttributes() {
		Shell shell = new Shell();
		SwtNamespace swt = SwtInfo.NAMESPACE;

		NamespaceDefinition testNsDef = new NamespaceDefinition("hallo");
		TagDefinition halloTagDef = new TagDefinition("hallo", swt.getTag("Composite"));
		testNsDef.defineTag(halloTagDef);
		testNsDef.defineForeignAttribute(new ForeignAttributeDefinition("hallo",
				SimpleTypes.STRING, swt.getTag("Button")));

		INamespaceResolver namespaceResolver = createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace(SwtNamespace.getNamespace().getUri())).andReturn(
				swt);
		expect(namespaceResolver.resolveNamespace("test")).andReturn(testNsDef);
		EasyMock.replay(namespaceResolver);

		new SwtXmlParser(shell, this).parse();
	}
}
