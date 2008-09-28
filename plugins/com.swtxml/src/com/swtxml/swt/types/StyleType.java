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
package com.swtxml.swt.types;

import java.util.Collection;
import java.util.List;

import com.swtxml.util.parser.ConstantParser;
import com.swtxml.util.proposals.Match;
import com.swtxml.util.types.IContentAssistable;
import com.swtxml.util.types.IType;

public class StyleType implements IType<Integer>, IContentAssistable {

	private ConstantParser constants;

	public StyleType(ConstantParser constants) {
		this.constants = constants;
	}

	public Integer convert(String value) {
		return constants.getIntValue(value);
	}

	public List<Match> getProposals(Match match) {
		return match.restrict(ConstantParser.SPLITTER).propose(getAllowedStyles());
	}

	public Collection<String> getAllowedStyles() {
		return constants.getConstants();
	}

}
