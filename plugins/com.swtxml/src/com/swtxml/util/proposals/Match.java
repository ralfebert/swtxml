package com.swtxml.util.proposals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.swtxml.util.parser.Splitter;

public class Match {

	private int cursorPos;
	private String text;
	private int start;
	private int end;

	public Match(String text, int cursorPos) {
		this.text = text;
		this.cursorPos = cursorPos;
		this.start = 0;
		this.end = text.length();
	}

	public Match(String str) {
		this.cursorPos = str.indexOf('§');
		this.text = str.substring(0, cursorPos) + str.substring(cursorPos + 1);
		this.start = 0;
		this.end = text.length();
	}

	public Match(Match m) {
		this.cursorPos = m.cursorPos;
		this.text = m.text;
		this.start = m.start;
		this.end = m.end;
	}

	public int getCursorPos() {
		return cursorPos;
	}

	public void setCursorPos(int cursorPos) {
		this.cursorPos = cursorPos;
	}

	public String getText() {
		return text.substring(start, end);
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text.substring(0, cursorPos) + "§" + text.substring(cursorPos);
	}

	private void stripQuotesR() {
		if (!text.startsWith("\"")) {
			insertR("\"", 0);
		}
		if (!text.endsWith("\"")) {
			insertR("\"", text.length());
		}
		this.start++;
		this.end--;
	}

	public Match handleQuotes() {
		Match m = new Match(this);
		m.stripQuotesR();
		return m;
	}

	public Match insert(String str, int i) {
		Match m = new Match(this);
		m.insertR(str, i);
		return m;
	}

	public Match replace(String str) {
		Match m = new Match(this);
		m.replaceR(str, 0, getText().length());
		return m;
	}

	public Match replace(String str, int i, int length) {
		Match m = new Match(this);
		m.replaceR(str, i, length);
		return m;
	}

	public Match restrict(Splitter splitter) {
		Match m = new Match(this);
		m.restrictR(splitter);
		return m;
	}

	private void insertR(String str, int i) {
		replaceR(str, i, 0);
	}

	private void replaceR(String str, int i, int length) {
		i += start;
		if (i + length < cursorPos) {
			cursorPos -= (length - str.length());
		}
		if (i <= cursorPos && cursorPos <= i + length) {
			cursorPos = i + str.length();
		}
		end -= (length - str.length());
		text = text.substring(0, i) + str + text.substring(i + length);
	}

	public String getReplacementText() {
		return text;
	}

	public List<Match> propose(String... values) {
		return propose(Arrays.asList(values));
	}

	private void restrictR(Splitter splitter) {
		splitter.getSeparators();
		for (int i = cursorPos; i >= start; i--) {
			if (splitter.isSeparator(text.charAt(i))) {
				start = i + 1;
				break;
			}
		}
		for (int i = cursorPos; i <= end; i++) {
			if (splitter.isSeparator(text.charAt(i))) {
				end = i;
				break;
			}
		}
	}

	public List<Match> propose(Collection<String> values) {
		final String textBeforeCursor = getTextBeforeCursor();
		List<String> filteredValues = new ArrayList<String>(Collections2.filter(values,
				new Predicate<String>() {

					public boolean apply(String value) {
						return value.toLowerCase().startsWith(textBeforeCursor.toLowerCase());
					}

				}));
		Collections.sort(filteredValues);
		ArrayList<Match> resultMatches = new ArrayList<Match>();
		for (String value : filteredValues) {
			resultMatches.add(this.replace(value));
		}
		return resultMatches;
	}

	public String getTextBeforeCursor() {
		if (cursorPos <= start) {
			return "";
		}
		return text.substring(start, cursorPos);
	}

	public void dump() {
		System.out.println(text);
		System.out.println(StringUtils.leftPad("[", start + 1)
				+ StringUtils.leftPad("]", end - start));
		System.out.println(StringUtils.leftPad("C", cursorPos + 1));
	}
}