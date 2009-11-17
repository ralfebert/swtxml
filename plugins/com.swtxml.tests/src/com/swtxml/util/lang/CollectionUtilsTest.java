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
package com.swtxml.util.lang;

import static com.swtxml.util.lang.Filters.and;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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

	private IFilter<Number> evenNumber() {
		return new IFilter<Number>() {
			public boolean match(Number n) {
				return n.longValue() % 2 == 0;
			}
		};
	}

	private IFilter<Number> is4Number() {
		return new IFilter<Number>() {
			public boolean match(Number n) {
				return n.longValue() == 4;
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

	@Test
	public void sortedToString() {
		assertEquals("1, 3, 5, 7", CollectionUtils.sortedToString(oddNumbers));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testAnd() {
		assertEquals("[2, 4, 6]", CollectionUtils.select(numbers,
				and(Arrays.asList(evenNumber(), evenNumber()))).toString());
		assertEquals("[4]", CollectionUtils.select(numbers,
				and(Arrays.asList(evenNumber(), is4Number()))).toString());
	}
}
