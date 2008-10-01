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

import com.swtxml.util.eclipse.EclipseEnvironmentJUnitTest;

/**
 * Test suite containing all tests which should be run in non-plugin environment
 * (Run as plain JUnit test)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { EclipseEnvironmentJUnitTest.class, AllTests.class })
public class AllJUnitTests {

}