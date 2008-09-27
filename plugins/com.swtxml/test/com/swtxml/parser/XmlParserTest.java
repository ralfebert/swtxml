package com.swtxml.parser;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.easymock.EasyMock;
import org.junit.Test;

import com.swtxml.definition.INamespaceResolver;
import com.swtxml.definition.impl.NamespaceDefinition;
import com.swtxml.definition.impl.TagDefinition;
import com.swtxml.tag.TagInformation;
import com.swtxml.util.types.SimpleTypes;

public class XmlParserTest {

	public static class CollectNumbers implements ITagProcessor {

		private String numbers = "";

		public String getNumbers() {
			return numbers;
		}

		public void process(TagInformation tag) {
			numbers += tag.getAttribute("no");
		}
	}

	private INamespaceResolver sampleNamespace() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition tag = testNamespace.defineTag("test");
		tag.defineAttribute("no", new SimpleTypes.StringType());

		INamespaceResolver namespaceResolver = EasyMock.createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace("test")).andReturn(testNamespace);

		replay(namespaceResolver);
		return namespaceResolver;
	}

	@Test
	public void testDepthFirstProcessorByProcessorOrder() {
		INamespaceResolver namespaceResolver = sampleNamespace();
		CollectNumbers collectNumbers = new CollectNumbers();
		TagLibraryXmlParser parser = new TagLibraryXmlParser(namespaceResolver, collectNumbers,
				collectNumbers);
		parser.parse("test", getClass().getResourceAsStream("numbers.xml"));
		assertEquals("123456123456", collectNumbers.getNumbers());
	}

	@Test
	public void testWrongTag() {
		INamespaceResolver namespaceResolver = sampleNamespace();
		TagLibraryXmlParser parser = new TagLibraryXmlParser(namespaceResolver);
		try {
			parser.parse("test", getClass().getResourceAsStream("wrongtag.xml"));
			fail("expected exception");
		} catch (XmlParsingException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("invalid"));
			assertTrue(e.getMessage().contains("test"));
		}
	}

	@Test
	public void testWrongAttribute() {
		INamespaceResolver namespaceResolver = sampleNamespace();
		TagLibraryXmlParser parser = new TagLibraryXmlParser(namespaceResolver);
		try {
			parser.parse("test", getClass().getResourceAsStream("wrongattribute.xml"));
			fail("expected exception");
		} catch (XmlParsingException e) {
			assertTrue(e.getMessage().contains("line 2"));
			assertTrue(e.getMessage().contains("invalid"));
			assertTrue(e.getMessage().contains("test"));
		}
	}

}
