package com.swtxml.util.types;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.swtxml.util.parser.ParseException;
import com.swtxml.util.properties.PropertyMatcher;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.proposals.Match;

public class SimpleTypes {

	public static void addSimpleTypes(PropertyRegistry inj) {
		inj.add(new PropertyMatcher(Boolean.class, Boolean.TYPE), new BooleanConverter());
		inj.add(new PropertyMatcher(Integer.class, Integer.TYPE), new IntegerConverter());
		inj.add(new PropertyMatcher(Float.class, Float.TYPE), new FloatConverter());
		inj.add(new PropertyMatcher(Character.class, Character.TYPE), new CharacterConverter());
		inj.add(new PropertyMatcher(String.class), new StringConverter());
		inj.add(new PropertyMatcher(int[].class), new IntArrayConverter());
	}

	public static class StringConverter implements IType<String> {
		public String convert(Object obj, String value) {
			// TODO: i18n
			if (value.startsWith("%")) {
				throw new ParseException("i18n for string properties is currently unsupported");
			}
			return value;
		}
	}

	public static class BooleanConverter implements IType<Boolean>, IContentAssistable {
		public Boolean convert(Object obj, String value) {
			return Boolean.parseBoolean(value);
		}

		public List<Match> getProposals(Match match) {
			return match.propose(String.valueOf(true), String.valueOf(false));
		}
	}

	public static class IntegerConverter implements IType<Integer> {
		public Integer convert(Object obj, String value) {
			return Integer.parseInt(value);
		}
	}

	public static class FloatConverter implements IType<Float> {
		public Float convert(Object obj, String value) {
			return Float.parseFloat(value);
		}
	}

	public static class CharacterConverter implements IType<Character> {
		public Character convert(Object obj, String value) {
			return value.charAt(0);
		}
	}

	public static class IntArrayConverter implements IType<int[]> {

		public int[] convert(Object obj, String value) {
			String[] intStrings = StringUtils.split(value, ',');
			int[] ints = new int[intStrings.length];
			for (int i = 0; i < ints.length; i++) {
				ints[i] = Integer.parseInt(intStrings[i]);
			}
			return ints;
		}

	}

}
