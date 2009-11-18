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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import com.swtxml.definition.IAttributeDefinition;
import com.swtxml.definition.INamespaceDefinition;
import com.swtxml.swt.SwtInfo;
import com.swtxml.swt.metadata.SwtNamespace;
import com.swtxml.swt.visitor.BuildWidgets;
import com.swtxml.tinydom.Tag;

public class BuildWidgetsTest {

	@Test
	public void testBuildWidgets() {

		Shell shell = new Shell();
		SwtNamespace swt = SwtInfo.NAMESPACE;

		Map<INamespaceDefinition, Map<IAttributeDefinition, String>> noAttributes = Collections
				.emptyMap();
		Tag root = new Tag(swt, swt.getTag("Composite"), noAttributes, null, "-");
		Tag btn = new Tag(swt, swt.getTag("Button"), noAttributes, root, "-");

		BuildWidgets builder = new BuildWidgets(shell);
		root.visitDepthFirst(builder);

		assertEquals(shell, root.getAdapter(Control.class));
		assertTrue(btn.getAdapter(Control.class) instanceof Button);
	}
}
