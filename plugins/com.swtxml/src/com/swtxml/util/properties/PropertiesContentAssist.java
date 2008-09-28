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
package com.swtxml.util.properties;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.swtxml.util.lang.CollectionUtils;
import com.swtxml.util.lang.IFunction;
import com.swtxml.util.parser.KeyValueContentAssist;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public abstract class PropertiesContentAssist extends KeyValueContentAssist {

	@Override
	protected List<Match> keyProposals(Map<String, String> values, Match match) {
		ClassProperties<?> properties = getClassProperties(values);
		if (properties == null) {
			return Collections.emptyList();
		}
		Set<String> propertyNames = new HashSet<String>(properties.getProperties().keySet());
		propertyNames.removeAll(values.keySet());
		Collection<String> propertyProposals = CollectionUtils.collect(propertyNames,
				new IFunction<String, String>() {
					public String apply(String s) {
						return s + ":";
					}
				});
		return match.propose(propertyProposals);
	}

	@Override
	protected List<Match> valueProposals(Map<String, String> values, String key, Match match) {
		ClassProperties<?> properties = getClassProperties(values);
		if (properties == null) {
			return Collections.emptyList();
		}
		Property property = properties.getProperties().get(key);
		if (property != null) {
			IType<?> type = property.getType();
			if (type instanceof IContentAssistable) {
				List<Match> proposals = ((IContentAssistable) type).getProposals(match);
				for (Match proposal : proposals) {
					// move cursor behind semicolon
					proposal.moveCursor(1);
				}
				return proposals;
			}
		}
		return Collections.emptyList();
	}

	protected abstract ClassProperties<?> getClassProperties(Map<String, String> values);

}
