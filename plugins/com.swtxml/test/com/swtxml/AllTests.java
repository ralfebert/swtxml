package com.swtxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.swtxml.converter.ConvertingInjectorTest;
import com.swtxml.converter.SwtConverterTest;
import com.swtxml.metadata.SwtTagRegistryTest;
import com.swtxml.util.KeyValueStringTest;
import com.swtxml.util.ReflectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { KeyValueStringTest.class, ConvertingInjectorTest.class, SwtHelperTest.class,
		ReflectorTest.class, SwtConverterTest.class, SwtTagRegistryTest.class, SwtWidgetsTest.class })
public class AllTests {

}
