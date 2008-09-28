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
package com.swtxml.util.parser;

import java.util.List;
import java.util.Map;

import com.swtxml.util.proposals.Match;

public abstract class KeyValueContentAssist {

	public final List<Match> getProposals(Match match) {
		String text = match.getText();
		int cursor = match.getCursorPos();

		if (cursor >= text.length() || match.getText().charAt(match.getCursorPos()) != ';') {
			match = match.insert(";", cursor);
			match.moveCursor(-1);
		}
		Map<String, String> values = KeyValueParser.parse(match.getText(), Strictness.NICE);
		Match keyValueMatch = match.restrict(KeyValueParser.VALUE_SPLITTER);

		text = keyValueMatch.getText();

		Splitter colon = KeyValueParser.KEY_VALUE_SPLITTER;
		int colonPos = text.indexOf(colon.getPreferredSeparator());

		if (colonPos < 0 || colonPos >= keyValueMatch.getCursorPos()) {
			return keyProposals(values, keyValueMatch.restrict(colon));
		} else {
			String[] keyValue = colon.split(text);
			return valueProposals(values, keyValue[0], keyValueMatch.restrict(colon));
		}
	}

	protected abstract List<Match> keyProposals(Map<String, String> values, Match match);

	protected abstract List<Match> valueProposals(Map<String, String> values, String key,
			Match match);

}
