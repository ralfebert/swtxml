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
package com.swtxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.swtxml.extensions.ExtensionsNamespaceResolverTest;
import com.swtxml.i18n.EclipsePluginLabelTranslatorTest;
import com.swtxml.util.eclipse.EclipseEnvironmentPluginTest;

/**
 * Test suite containing all tests which should be run in a plugin environment
 * (Run via PDE as JUnit Plugin test)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { EclipseEnvironmentPluginTest.class, AllTests.class,
		ExtensionsNamespaceResolverTest.class, EclipsePluginLabelTranslatorTest.class })
public class AllPluginTests {

}