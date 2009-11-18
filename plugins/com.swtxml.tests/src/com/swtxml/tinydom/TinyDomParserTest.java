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
import com.swtxml.definition.ITagDefinition;
import com.swtxml.definition.impl.AttributeDefinition;
import com.swtxml.definition.impl.ForeignAttributeDefinition;
import com.swtxml.definition.impl.NamespaceDefinition;
import com.swtxml.definition.impl.TagDefinition;
import com.swtxml.resources.ClassResource;
import com.swtxml.util.parser.ParseException;
import com.swtxml.util.types.SimpleTypes;

public class TinyDomParserTest {

	public static class CollectNumbers implements ITagVisitor {

		private String numbers = "";

		public String getNumbers() {
			return numbers;
		}

		public void visit(Tag tag) {
			numbers += tag.getAttribute("no");
		}
	}

	private INamespaceResolver sampleNamespace() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition tag = new TagDefinition("test", ITagDefinition.ROOT).allowNested();
		testNamespace.defineTag(tag);
		tag.defineAttribute(new AttributeDefinition("no", SimpleTypes.STRING));
		return namespace("test", testNamespace);
	}

	private INamespaceResolver namespace(String name, NamespaceDefinition testNamespace) {
		INamespaceResolver namespaceResolver = createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace(name)).andReturn(testNamespace);
		replay(namespaceResolver);
		return namespaceResolver;
	}

	@Test
	public void testDepthFirstTagProcessing() {
		Tag root = parseNumbers();
		CollectNumbers collectNumbers = new CollectNumbers();
		root.visitDepthFirst(collectNumbers, collectNumbers);
		assertEquals("112233445566", collectNumbers.getNumbers());
	}

	@Test
	public void testHierarchy() {
		Tag root = parseNumbers();

		assertNull(root.getParent());
		assertEquals(2, root.getChildren().size());
		assertEquals(root, root.getChildren().get(0).getParent());
		assertEquals("test", root.getName());
	}

	@Test
	public void testAttributes() {
		Tag root = parseNumbers();
		assertEquals("1", root.getAttribute("no"));
	}

	@Test
	public void testForeignAttributes() {
		Tag root = parseNumbers();
		assertEquals("1", root.getAttribute("no"));
	}

	@Test
	public void testAdapters() {
		Tag no1 = parseNumbers();
		Tag no2 = no1.getChildren().get(0);
		Tag no3 = no2.getChildren().get(0);
		Tag no4 = no3.getChildren().get(0);

		String str2 = "Hallo";
		no2.addAdapter(str2);

		String str1 = "xxx";
		no1.addAdapter(str1);

		assertEquals(null, no4.getAdapter(String.class));
		assertEquals(null, no3.getAdapter(String.class));
		assertEquals(str2, no2.getAdapter(String.class));
		assertEquals(str1, no1.getAdapter(String.class));

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
		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"numbers.xml"));
		Tag root = parser.parse();
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
		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"wrongtag.xml"));
		try {
			parser.parse();
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
		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"wrongattribute.xml"));
		try {
			parser.parse();
			fail("expected exception");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("invalid"));
			assertTrue(e.getMessage().contains("test"));
		}
	}

	@Test
	public void testProcessingError() {
		ITagVisitor visitor = createMock(ITagVisitor.class);
		visitor.visit((Tag) anyObject());
		expectLastCall().andThrow(new ParseException("NO"));
		replay(visitor);

		INamespaceResolver namespaceResolver = sampleNamespace();
		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"numbers.xml"));
		Tag root = parser.parse();
		try {
			root.visitDepthFirst(visitor);
			fail("expected");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("NO"));
		}
	}

	@Test
	public void testInvalidScope() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition test = new TagDefinition("test", ITagDefinition.ROOT);
		testNamespace.defineTag(test);
		testNamespace.defineTag(new TagDefinition("yes", test));

		INamespaceResolver namespaceResolver = namespace("test", testNamespace);
		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"invalidscope.xml"));
		try {
			parser.parse();
			fail("expected exception");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 4"));
			assertTrue(e.getMessage().contains("test"));
		}
	}

	@Test
	public void testForeignAttribute() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition test = new TagDefinition("test", ITagDefinition.ROOT);
		test.defineAttribute(new AttributeDefinition("hallo", SimpleTypes.STRING));
		testNamespace.defineTag(test);

		NamespaceDefinition attrSpace = new NamespaceDefinition();

		attrSpace.defineForeignAttribute(new ForeignAttributeDefinition("hallo",
				SimpleTypes.STRING, test));

		INamespaceResolver namespaceResolver = createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace("test")).andReturn(testNamespace);
		expect(namespaceResolver.resolveNamespace("attrs")).andReturn(attrSpace);
		replay(namespaceResolver);

		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"foreignattributes.xml"));

		Tag element = parser.parse();
		assertEquals("tag-welt", element.getAttribute("hallo"));
		assertEquals("tag-welt", element.getAttribute(testNamespace, "hallo"));
		assertEquals("namespace-welt", element.getAttribute(attrSpace, "hallo"));
	}

	@Test
	public void testInvalidForeignAttribute() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition test = new TagDefinition("test", ITagDefinition.ROOT);
		test.defineAttribute(new AttributeDefinition("hallo", SimpleTypes.STRING));
		testNamespace.defineTag(test);

		NamespaceDefinition attrSpace = new NamespaceDefinition();

		INamespaceResolver namespaceResolver = createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace("test")).andReturn(testNamespace);
		expect(namespaceResolver.resolveNamespace("attrs")).andReturn(attrSpace);
		replay(namespaceResolver);

		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"foreignattributes.xml"));

		try {
			parser.parse();
			fail("expected exception");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("f:hallo"));
			assertTrue(e.getMessage().contains("test"));
		}
	}

	@Test
	public void testInvalidScopedForeignAttribute() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition test = new TagDefinition("test", ITagDefinition.ROOT);
		test.defineAttribute(new AttributeDefinition("hallo", SimpleTypes.STRING));
		testNamespace.defineTag(test);
		TagDefinition yes = new TagDefinition("yes", test);
		testNamespace.defineTag(yes);

		NamespaceDefinition attrSpace = new NamespaceDefinition();
		attrSpace.defineForeignAttribute(new ForeignAttributeDefinition("hallo",
				SimpleTypes.STRING, yes));

		INamespaceResolver namespaceResolver = createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace("test")).andReturn(testNamespace);
		expect(namespaceResolver.resolveNamespace("attrs")).andReturn(attrSpace);
		replay(namespaceResolver);

		TinyDomParser parser = new TinyDomParser(namespaceResolver, new ClassResource(getClass(),
				"foreignattributes.xml"));

		try {
			parser.parse();
			fail("expected exception");
		} catch (ParseException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("f:hallo"));
			assertTrue(e.getMessage().contains("test"));
		}
	}
}
