package com.swtxml.swt.types;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IFunction;
import com.swtxml.util.parser.KeyValueParser;
import com.swtxml.util.parser.Splitter;
import com.swtxml.util.parser.Strictness;
import com.swtxml.util.properties.ClassProperties;
import com.swtxml.util.properties.Property;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class PropertiesContentAssist {

	private final Match keyValueMatch;
	private final Match fullMatch;
	private final Map<String, String> propertyValues;

	public PropertiesContentAssist(Match match) {
		String text = match.getText();
		int cursor = match.getCursorPos();

		if (cursor >= text.length() || match.getText().charAt(match.getCursorPos()) != ';') {
			match = match.insert(";", cursor);
			match.moveCursor(-1);
		}
		this.fullMatch = match;
		this.propertyValues = KeyValueParser.parse(match.getText(), Strictness.LAX);
		this.keyValueMatch = match.restrict(KeyValueParser.VALUE_SPLITTER);
	}

	public List<Match> getProposals(ClassProperties<?> properties) {
		Match match = keyValueMatch;
		String text = match.getText();
		int cursor = match.getCursorPos();

		Splitter colon = KeyValueParser.KEY_VALUE_SPLITTER;
		int colonPos = text.indexOf(colon.getPreferredSeparator());

		if (colonPos < 0 || colonPos >= cursor) {
			Set<String> propertyNames = new HashSet<String>(properties.getProperties().keySet());
			propertyNames.removeAll(propertyValues.keySet());
			Collection<String> propertyProposals = CollectionUtils.collect(propertyNames,
					new IFunction<String, String>() {
						public String apply(String s) {
							return s + ":";
						}
					});
			return match.propose(propertyProposals);
		} else {
			String[] keyValue = colon.split(text);
			Property property = properties.getProperties().get(keyValue[0]);
			if (property != null) {
				IType<?> type = property.getType();
				if (type instanceof IContentAssistable) {
					List<Match> proposals = ((IContentAssistable) type).getProposals(match
							.restrict(colon));
					for (Match proposal : proposals) {
						// move cursor behind semicolon
						proposal.moveCursor(1);
					}
					return proposals;
				}
			}
		}
		return Collections.emptyList();
	}

	public Match getKeyValueMatch() {
		return keyValueMatch;
	}

	public Match getFullMatch() {
		return fullMatch;
	}

	public Map<String, String> getPropertyValues() {
		return propertyValues;
	}

}
