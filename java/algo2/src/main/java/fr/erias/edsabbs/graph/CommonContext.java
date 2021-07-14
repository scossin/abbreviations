package fr.erias.edsabbs.graph;

import fr.erias.abbsdetection.nounphrases.TF;

public class CommonContext implements ICommonContext {
	private TF termShortForm;
	private TF termLongForm;
	private String contextWord;
	
	public CommonContext(TF termLongForm, TF termShortForm, String contextWord) {
		this.termLongForm = termLongForm;
		this.termShortForm = termShortForm;
		this.contextWord = contextWord;
	}
	
	@Override
	public TF getTermShortForm() {
		return termShortForm;
	}

	@Override
	public TF getTermLongForm() {
		return termLongForm;
	}

	@Override
	public String getContextWord() {
		return contextWord;
	}
}
