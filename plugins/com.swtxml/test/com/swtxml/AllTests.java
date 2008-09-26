package com.swtxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.swtxml.swt.injector.FormAttachmentConverterTest;
import com.swtxml.swt.injector.SwtConvertersTest;
import com.swtxml.swt.injector.SwtInjectorTest;
import com.swtxml.swt.metadata.SwtNamespaceTest;
import com.swtxml.swt.sample.SwtWidgetsTest;
import com.swtxml.util.injector.BaseConvertersTest;
import com.swtxml.util.injector.InjectorTest;
import com.swtxml.util.injector.PropertyMatcherTest;
import com.swtxml.util.parser.ConstantParserTest;
import com.swtxml.util.parser.KeyValueParserTest;
import com.swtxml.util.reflector.ReflectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { ReflectorTest.class, PropertyMatcherTest.class, BaseConvertersTest.class,
		InjectorTest.class, ConstantParserTest.class, KeyValueParserTest.class,
		FormAttachmentConverterTest.class, SwtConvertersTest.class, SwtInjectorTest.class,
		SwtNamespaceTest.class, SwtWidgetsTest.class })
public class AllTests {

}