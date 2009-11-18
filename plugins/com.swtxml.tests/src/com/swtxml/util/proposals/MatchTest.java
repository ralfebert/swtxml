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
package com.swtxml.util.proposals;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.swtxml.util.parser.Splitter;

public class MatchTest {

	private Match m;
	private Match m2;

	@Before
	public void setup() {
		m = new Match("123456", 3);
		m2 = new Match("\"123§456\"");
	}

	@Test
	public void testMatch() {
		assertEquals("123§456", m.toString());
		assertEquals("123§456", new Match(m.toString()).toString());
	}

	@Test
	public void testReplaceBeforeCursor() {
		assertEquals("1xxx3§456", m.replace("xxx", 1, 1).toString());
	}

	@Test
	public void testReplaceAfterCursor() {
		assertEquals("123§4xxx6", m.replace("xxx", 4, 1).toString());
	}

	@Test
	public void testInsertBeforeCursor() {
		assertEquals("1xxx23§456", m.insert("xxx", 1).toString());
	}

	@Test
	public void testInsertAfterCursor() {
		assertEquals("123§4xxx56", m.insert("xxx", 4).toString());
	}

	@Test
	public void testInsertAroundMatch() {
		m = m.insertAroundMatch("yy", "xxx");
		assertEquals("yy123§456xxx", m.toString());
		assertEquals("123456", m.getText());
	}

	@Test
	public void handleQuotes() {
		assertEquals("\"123§456\"", m.handleQuotes().toString());
		assertEquals("\"123§456\"", m2.handleQuotes().toString());
		assertEquals("123456", m.handleQuotes().getText());
		assertEquals("123456", m2.handleQuotes().getText());
		assertEquals("\"123456\"", m.handleQuotes().getReplacementText());
		assertEquals("\"123456\"", m2.handleQuotes().getReplacementText());
	}

	@Test
	public void testInsertAroundMatchWithQuotes() {
		m = m.handleQuotes().insertAroundMatch("yy", "xxx");
		assertEquals("\"yy123§456xxx\"", m.toString());
		assertEquals("123456", m.getText());
	}

	@Test
	public void insertWorksWithOffsets() {
		assertEquals("\"12xxx3§456\"", m2.handleQuotes().insert("xxx", 2).toString());
	}

	@Test
	public void restrict() {
		m = new Match("123,456.7§89,012");
		m = m.restrict(new Splitter(",."));
		assertEquals("789", m.getText());
		m = m.replace("xxx");
		assertEquals("xxx", m.getText());
		assertEquals("123,456.xxx§,012", m.toString());
	}

	@Test
	public void getTextBeforeCursor() {
		assertEquals("7", new Match("123,456.7§89,012").restrict(new Splitter(",."))
				.getTextBeforeCursor());
		assertEquals("", new Match("123,456.§789,012").restrict(new Splitter(",."))
				.getTextBeforeCursor());
	}

	@Test
	public void testProposeNoResults() {
		m = new Match("123,456.7§89,012").restrict(new Splitter(",."));
		List<Match> proposals = m.propose("red", "green", "blue");
		assertEquals(0, proposals.size());
	}

	@Test
	public void testProposeFiltered() {
		m = new Match("123,456.r§89,012").restrict(new Splitter(",."));
		List<Match> proposals = m.propose("red", "green", "blue");
		assertEquals(1, proposals.size());
		assertEquals("red", proposals.get(0).getText());
		assertEquals("123,456.red§,012", proposals.get(0).toString());
	}

	@Test
	public void testProposeAll() {
		m = new Match("123,456.§89,012").restrict(new Splitter(",."));
		List<Match> proposals = m.propose("red", "green", "blue");
		assertEquals(3, proposals.size());
		assertEquals("blue", proposals.get(0).getText());
		assertEquals("123,456.blue§,012", proposals.get(0).toString());
	}

	@Test
	public void testCursorAfterReplacement() {
		assertEquals("test§", new Match("§").replace("test").toString());
		assertEquals("test§", new Match("t§").replace("test").toString());
		assertEquals("\"test§\"", new Match("\"§\"").handleQuotes().replace("test").toString());
		assertEquals("\"test§\"", new Match("\"t§\"").handleQuotes().replace("test").toString());
	}

}