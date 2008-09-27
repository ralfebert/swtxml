package com.swtxml.util.collections;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IFunction;
import com.swtxml.util.lang.IPredicate;

public class CollectionUtilsTest {

	private List<Integer> numbers;
	private List<Integer> oddNumbers;
	private List<Integer> emptyList = new ArrayList<Integer>();

	@Before
	public void setup() {
		numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
		oddNumbers = Arrays.asList(1, 3, 5, 7);
	}

	@Test
	public void testFind() {
		assertEquals(2, CollectionUtils.find(numbers, evenNumber()));
		assertEquals(null, CollectionUtils.find(oddNumbers, evenNumber()));
	}

	private IPredicate<Number> evenNumber() {
		return new IPredicate<Number>() {
			public boolean match(Number n) {
				return n.longValue() % 2 == 0;
			}
		};
	}

	private IFunction<Number, Number> plus1() {
		return new IFunction<Number, Number>() {
			public Number apply(Number n) {
				return n.longValue() + 1;
			}
		};
	}

	@Test
	public void testSelect() {
		assertEquals("[2, 4, 6]", CollectionUtils.select(numbers, evenNumber()).toString());
		assertEquals("[]", CollectionUtils.select(oddNumbers, evenNumber()).toString());
	}

	@Test
	public void testCollect() {
		assertEquals("[2, 4, 6, 8]", CollectionUtils.collect(oddNumbers, plus1()).toString());
		assertEquals("[]", CollectionUtils.collect(emptyList, plus1()).toString());
	}

}
