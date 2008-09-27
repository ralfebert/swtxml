package com.swtxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.swtxml.swt.injector.FormAttachmentConverterTest;
import com.swtxml.swt.injector.SwtConvertersTest;
import com.swtxml.swt.injector.SwtInjectorTest;
import com.swtxml.swt.metadata.SwtNamespaceTest;
import com.swtxml.swt.sample.SwtWidgetsTest;
import com.swtxml.util.parser.ConstantParserTest;
import com.swtxml.util.parser.KeyValueParserTest;
import com.swtxml.util.parser.SplitterTest;
import com.swtxml.util.properties.BaseConvertersTest;
import com.swtxml.util.properties.PropertyMatcherTest;
import com.swtxml.util.properties.PropertyRegistryTest;
import com.swtxml.util.proposals.ProposalsTest;
import com.swtxml.util.reflector.ReflectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { SplitterTest.class, ProposalsTest.class, ReflectorTest.class,
		PropertyMatcherTest.class, BaseConvertersTest.class, PropertyRegistryTest.class,
		ConstantParserTest.class, KeyValueParserTest.class, FormAttachmentConverterTest.class,
		SwtConvertersTest.class, SwtInjectorTest.class, SwtNamespaceTest.class,
		SwtWidgetsTest.class })
public class AllTests {

}