package com.swtxml.parser;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

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

	@Test
	public void testDepthFirstProcessorByProcessorOrder() {
		NamespaceDefinition testNamespace = new NamespaceDefinition();
		TagDefinition tag = testNamespace.defineTag("test");
		tag.defineAttribute("no", new SimpleTypes.StringType());

		INamespaceResolver namespaceResolver = EasyMock.createMock(INamespaceResolver.class);
		expect(namespaceResolver.resolveNamespace("test")).andReturn(testNamespace);

		replay(namespaceResolver);

		CollectNumbers collectNumbers = new CollectNumbers();
		TagLibraryXmlParser parser = new TagLibraryXmlParser(namespaceResolver, collectNumbers,
				collectNumbers);
		parser.parse("test", getClass().getResourceAsStream("numbers.xml"));
		assertEquals("123456123456", collectNumbers.getNumbers());
	}
}
