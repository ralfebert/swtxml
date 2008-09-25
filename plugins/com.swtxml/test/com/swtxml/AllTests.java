package com.swtxml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.swtxml.metadata.SwtTagRegistryTest;
import com.swtxml.util.ReflectorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses( { SwtHelperTest.class, ReflectorTest.class, SwtTagRegistryTest.class,
		SwtWidgetsTest.class })
public class AllTests {

}
