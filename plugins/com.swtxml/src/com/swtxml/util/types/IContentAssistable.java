package com.swtxml.util.types;

import java.util.List;

import com.swtxml.util.proposals.Match;

public interface IContentAssistable {

	public List<Match> getProposals(Match match);

}
