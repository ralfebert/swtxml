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
package com.swtxml.util.types;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.swtxml.util.properties.PropertyMatcher;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.proposals.Match;

public class SimpleTypes {

	public static final IType<String> STRING = new StringType();
	public static final IType<Boolean> BOOLEAN = new BooleanType();
	public static final IType<Integer> INTEGER = new IntegerType();
	public static final IType<Float> FLOAT = new FloatType();
	public static final IType<Character> CHARACTER = new CharacterType();
	public static final IType<int[]> INT_ARRAY = new IntArrayType();

	public static void addSimpleTypes(PropertyRegistry inj) {
		inj.add(new PropertyMatcher(Boolean.class, Boolean.TYPE), BOOLEAN);
		inj.add(new PropertyMatcher(Integer.class, Integer.TYPE), INTEGER);
		inj.add(new PropertyMatcher(Float.class, Float.TYPE), FLOAT);
		inj.add(new PropertyMatcher(Character.class, Character.TYPE), CHARACTER);
		inj.add(new PropertyMatcher(String.class), STRING);
		inj.add(new PropertyMatcher(int[].class), INT_ARRAY);
	}

	private static class StringType implements IType<String> {
		public String convert(String value) {
			return value;
		}
	}

	private static class BooleanType implements IType<Boolean>, IContentAssistable {
		public Boolean convert(String value) {
			return Boolean.parseBoolean(value);
		}

		public List<Match> getProposals(Match match) {
			return match.propose(String.valueOf(true), String.valueOf(false));
		}
	}

	private static class IntegerType implements IType<Integer> {
		public Integer convert(String value) {
			return Integer.parseInt(value);
		}
	}

	private static class FloatType implements IType<Float> {
		public Float convert(String value) {
			return Float.parseFloat(value);
		}
	}

	private static class CharacterType implements IType<Character> {
		public Character convert(String value) {
			return value.charAt(0);
		}
	}

	private static class IntArrayType implements IType<int[]> {

		public int[] convert(String value) {
			String[] intStrings = StringUtils.split(value, ',');
			int[] ints = new int[intStrings.length];
			for (int i = 0; i < ints.length; i++) {
				ints[i] = Integer.parseInt(intStrings[i]);
			}
			return ints;
		}

	}

}
