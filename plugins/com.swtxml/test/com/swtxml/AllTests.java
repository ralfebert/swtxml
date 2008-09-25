package com.swtxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.swtxml.converter.InjectorTest;
import com.swtxml.converter.PropertyMatcherTest;
import com.swtxml.converter.ConvertersTest;
import com.swtxml.metadata.SwtTagRegistryTest;
import com.swtxml.util.KeyValueStringTest;
import com.swtxml.util.ReflectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { KeyValueStringTest.class, PropertyMatcherTest.class,
		InjectorTest.class, SwtHelperTest.class, ReflectorTest.class,
		ConvertersTest.class, SwtTagRegistryTest.class, SwtWidgetsTest.class })
public class AllTests {

}
