package com.swtxml.util.types;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.swtxml.util.parser.ParseException;
import com.swtxml.util.properties.PropertyMatcher;
import com.swtxml.util.properties.PropertyRegistry;
import com.swtxml.util.proposals.Match;

public class SimpleTypes {

	// TODO: keep types static
	public static void addSimpleTypes(PropertyRegistry inj) {
		inj.add(new PropertyMatcher(Boolean.class, Boolean.TYPE), new BooleanType());
		inj.add(new PropertyMatcher(Integer.class, Integer.TYPE), new IntegerType());
		inj.add(new PropertyMatcher(Float.class, Float.TYPE), new FloatType());
		inj.add(new PropertyMatcher(Character.class, Character.TYPE), new CharacterType());
		inj.add(new PropertyMatcher(String.class), new StringType());
		inj.add(new PropertyMatcher(int[].class), new IntArrayType());
	}

	public static class StringType implements IType<String> {
		public String convert(String value) {
			// TODO: i18n
			if (value.startsWith("%")) {
				throw new ParseException("i18n for string properties is currently unsupported");
			}
			return value;
		}
	}

	public static class BooleanType implements IType<Boolean>, IContentAssistable {
		public Boolean convert(String value) {
			return Boolean.parseBoolean(value);
		}

		public List<Match> getProposals(Match match) {
			return match.propose(String.valueOf(true), String.valueOf(false));
		}
	}

	public static class IntegerType implements IType<Integer> {
		public Integer convert(String value) {
			return Integer.parseInt(value);
		}
	}

	public static class FloatType implements IType<Float> {
		public Float convert(String value) {
			return Float.parseFloat(value);
		}
	}

	public static class CharacterType implements IType<Character> {
		public Character convert(String value) {
			return value.charAt(0);
		}
	}

	public static class IntArrayType implements IType<int[]> {

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
