package com.swtxml.tinydom;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.definition.impl.NamespaceDefinition;
import com.swtxml.definition.impl.TagDefinition;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.types.SimpleTypes;

public class TinyDomParserTest {

	public static class CollectNumbers implements ITagProcessor {

		private String numbers = "";

		public String getNumbers() {
			return numbers;
		}

		public void process(Tag tag) {
			numbers += tag.getAttribute("no");
		}
	}

	private INamespaceResolver sampleNamespace() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition tag = testNamespace.defineTag("test");
		tag.defineAttribute("no", SimpleTypes.STRING);

		INamespaceResolver namespaceResolver = createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace("test")).andReturn(testNamespace);

		replay(namespaceResolver);
		return namespaceResolver;
	}

	@Test
	public void testDepthFirstTagProcessing() {
		Tag root = parseNumbers();
		CollectNumbers collectNumbers = new CollectNumbers();
		root.depthFirst(collectNumbers, collectNumbers);
		assertEquals("112233445566", collectNumbers.getNumbers());
	}

	@Test
	public void testHierarchy() {
		Tag root = parseNumbers();

		assertNull(root.getParent());
		assertEquals(2, root.getChildren().size());
		assertEquals(root, root.getChildren().get(0).getParent());
		assertEquals("test", root.getTagName());
	}

	@Test
	public void testAttributes() {
		Tag root = parseNumbers();
		assertEquals("1", root.getAttribute("no"));
		assertEquals("1", root.getAttribute("no"));
		assertEquals("1", root.slurpAttribute("no"));
		assertEquals(null, root.getAttribute("no"));
	}

	@Test
	public void testAdapters() {
		Tag no1 = parseNumbers();
		Tag no2 = no1.getChildren().get(0);
		Tag no3 = no2.getChildren().get(0);
		Tag no4 = no3.getChildren().get(0);

		String str2 = "Hallo";
		no2.makeAdaptable(str2);

		String str1 = "xxx";
		no1.makeAdaptable(str1);

		assertEquals(null, no4.adaptTo(String.class));
		assertEquals(null, no3.adaptTo(String.class));
		assertEquals(str2, no2.adaptTo(String.class));
		assertEquals(str1, no1.adaptTo(String.class));

		assertEquals(0, no4.adaptChildren(String.class).size());
		assertEquals(0, no3.adaptChildren(String.class).size());
		assertEquals(0, no2.adaptChildren(String.class).size());
		assertArrayEquals(new Object[] { str2 }, no1.adaptChildren(String.class).toArray());

		assertEquals(null, no4.parentAdaptTo(String.class));
		assertEquals(str2, no3.parentAdaptTo(String.class));
		assertEquals(str1, no2.parentAdaptTo(String.class));
		assertEquals(null, no1.parentAdaptTo(String.class));

		assertEquals(str2, no4.parentRecursiveAdaptTo(String.class));
		assertEquals(str2, no3.parentRecursiveAdaptTo(String.class));
		assertEquals(str1, no2.parentRecursiveAdaptTo(String.class));
		assertEquals(null, no1.parentRecursiveAdaptTo(String.class));
	}

	private Tag parseNumbers() {
		INamespaceResolver namespaceResolver = sampleNamespace();
		TinyDomParser parser = new TinyDomParser(namespaceResolver);
		Tag root = parser.parse("numbers.xml", getClass().getResourceAsStream("numbers.xml"));
		return root;
	}

	@Test
	public void testLocationInfo() {
		Tag root = parseNumbers();
		assertEquals("numbers.xml [line 2] ", root.getLocationInfo());
	}

	@Test
	public void testWrongTag() {
		INamespaceResolver namespaceResolver = sampleNamespace();
		TinyDomParser parser = new TinyDomParser(namespaceResolver);
		try {
			parser.parse("test", getClass().getResourceAsStream("wrongtag.xml"));
			fail("expected exception");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("invalid"));
			assertTrue(e.getMessage().contains("test"));
		}
	}

	@Test
	public void testWrongAttribute() {
		INamespaceResolver namespaceResolver = sampleNamespace();
		TinyDomParser parser = new TinyDomParser(namespaceResolver);
		try {
			parser.parse("test", getClass().getResourceAsStream("wrongattribute.xml"));
			fail("expected exception");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("invalid"));
			assertTrue(e.getMessage().contains("test"));
		}
	}

	@Test
	public void testProcessingError() {
		ITagProcessor tagProcessor = createMock(ITagProcessor.class);
		tagProcessor.process((Tag) anyObject());
		expectLastCall().andThrow(new ParseException("NO"));
		replay(tagProcessor);

		INamespaceResolver namespaceResolver = sampleNamespace();
		TinyDomParser parser = new TinyDomParser(namespaceResolver);
		Tag root = parser.parse("test", getClass().getResourceAsStream("numbers.xml"));
		try {
			root.depthFirst(tagProcessor);
			fail("expected");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("NO"));
		}
	}
}
