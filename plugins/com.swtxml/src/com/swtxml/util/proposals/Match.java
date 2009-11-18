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
package com.swtxml.util.proposals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IFilter;
import com.swtxml.util.parser.ParseException;
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
		if (this.cursorPos < 0) {
			throw new ParseException("No cursor char § given");
		}
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

	public int getReplacementCursorPos() {
		return cursorPos;
	}

	public int getCursorPos() {
		return cursorPos - start;
	}

	public String getText() {
		return text.substring(start, end);
	}

	@Override
	public String toString() {
		return text.substring(0, cursorPos) + "§" + text.substring(cursorPos);
	}

	private void _handleQuotes() {
		if (!text.startsWith("\"")) {
			_insertAroundMatch("\"", "");
		} else {
			this.start++;
		}
		if (!text.endsWith("\"")) {
			_insertAroundMatch("", "\"");
		} else {
			this.end--;
		}
	}

	public Match handleQuotes() {
		Match m = new Match(this);
		m._handleQuotes();
		return m;
	}

	public Match insert(String str, int i) {
		Match m = new Match(this);
		m._insert(str, i);
		return m;
	}

	public Match replace(String str) {
		Match m = new Match(this);
		m._replace(str, 0, getText().length());
		return m;
	}

	public Match replace(String str, int i, int length) {
		Match m = new Match(this);
		m._replace(str, i, length);
		return m;
	}

	public Match restrict(Splitter splitter) {
		Match m = new Match(this);
		m._restrict(splitter);
		return m;
	}

	private void _insert(String str, int i) {
		_replace(str, i, 0);
	}

	private void _replace(String str, int i, int length) {
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

	public Match insertAroundMatch(String startstr, String endstr) {
		Match m = new Match(this);
		m._insertAroundMatch(startstr, endstr);
		return m;
	}

	private void _insertAroundMatch(String startstr, String endstr) {
		text = text.substring(0, start) + startstr + text.substring(start, end) + endstr
				+ text.substring(end);
		start += startstr.length();
		end += startstr.length();
		cursorPos += startstr.length();
	}

	public String getReplacementText() {
		return text;
	}

	public List<Match> propose(String... values) {
		return propose(Arrays.asList(values));
	}

	private void _restrict(Splitter splitter) {
		splitter.getSeparators();
		for (int i = cursorPos - 1; i >= start; i--) {
			if (splitter.isSeparator(text.charAt(i))) {
				start = i + 1;
				break;
			}
		}
		for (int i = cursorPos; i < end; i++) {
			if (splitter.isSeparator(text.charAt(i))) {
				end = i;
				break;
			}
		}
	}

	public List<Match> propose(Collection<String> values) {
		final String textBeforeCursor = getTextBeforeCursor().toLowerCase().trim();
		List<String> filteredValues = new ArrayList<String>(CollectionUtils.select(values,
				new IFilter<String>() {

					public boolean match(String value) {
						return value.toLowerCase().startsWith(textBeforeCursor);
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

	public void moveCursor(int i) {
		cursorPos += i;
	}
}