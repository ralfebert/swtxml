package com.swtxml.swt.types;

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
		return match.restrict(ConstantParser.SPLITTER).propose(constants.getConstants());
	}

}
