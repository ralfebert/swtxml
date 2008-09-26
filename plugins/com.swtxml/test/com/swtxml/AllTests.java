package com.swtxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.swtxml.converter.ConvertersTest;
import com.swtxml.converter.FormAttachmentConverterTest;
import com.swtxml.converter.InjectorTest;
import com.swtxml.converter.PropertyMatcherTest;
import com.swtxml.converter.SwtInjectorTest;
import com.swtxml.metadata.SwtTagRegistryTest;
import com.swtxml.swt.SwtConstantsTest;
import com.swtxml.util.KeyValueStringTest;
import com.swtxml.util.ReflectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { KeyValueStringTest.class, PropertyMatcherTest.class, InjectorTest.class,
		SwtInjectorTest.class, SwtHelperTest.class, SwtConstantsTest.class,
		FormAttachmentConverterTest.class, ReflectorTest.class, ConvertersTest.class,
		SwtTagRegistryTest.class, SwtWidgetsTest.class })
public class AllTests {

}
