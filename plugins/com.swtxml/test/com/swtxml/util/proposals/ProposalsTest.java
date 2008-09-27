package com.swtxml.util.proposals;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ProposalsTest {

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
	public void stripQuotes() {
		assertEquals("\"123§456\"", m.stripQuotes().toString());
		assertEquals("\"123§456\"", m2.stripQuotes().toString());
		assertEquals("123456", m.stripQuotes().getText());
		assertEquals("123456", m2.stripQuotes().getText());
		assertEquals("\"123456\"", m.stripQuotes().getReplacementText());
		assertEquals("\"123456\"", m2.stripQuotes().getReplacementText());
	}

	@Test
	public void insertWorksWithOffsets() {
		assertEquals("\"12xxx3§456\"", m2.stripQuotes().insert("xxx", 2).toString());
	}
}