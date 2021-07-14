package fr.erias.abbsdetection.nounphrases;

import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.IAMsystem.terminology.Terminology;
import fr.erias.IAMsystem.tokenizernormalizer.ITokenizerNormalizer;
import fr.erias.IAMsystem.tokenizernormalizer.TNoutput;
import fr.erias.IAMsystem.tokenizernormalizer.TokenizerNormalizer;

public class Terminology4detection {

	private ITokenizerNormalizer tokenizerNormalizer = TokenizerNormalizer.getDefaultTokenizerNormalizer();
	
	private Terminology terminology;
	
	public Terminology getTerminology() {
		return(this.terminology);
	}
	
	public Terminology4detection(IGetSimpleTerms simpleTermsProvider) {
		this.terminology = getTerminologyRemoveUnigram(tokenizerNormalizer, simpleTermsProvider);
	}
	
	public Terminology getTerminologyRemoveUnigram(ITokenizerNormalizer tokenizerNormalizer, IGetSimpleTerms simpleTermsProvider) {
		Terminology terminology = new Terminology();
		for (ISimpleTerm simpleTerm : simpleTermsProvider.getSimpleTerms()) {
			if (isAterm2ignore(simpleTerm, tokenizerNormalizer)) {
				continue;
			}
			TNoutput tnoutput = tokenizerNormalizer.tokenizeNormalize(simpleTerm.getWord());
			terminology.addTerm(tnoutput.getNormalizedSentence(), simpleTerm.getWord());
		}
		return(terminology);
	}
	
	private boolean isAterm2ignore(ISimpleTerm simpleTerm, ITokenizerNormalizer tokenizerNormalizer) {
		if (simpleTerm.getWord().equals("")) {
			return true;
		}
		TNoutput tnoutput = tokenizerNormalizer.tokenizeNormalize(simpleTerm.getWord());
		if (isAunigram(tnoutput.getTokens())) {
			return true;
		}
		IStopwords stopwords = tokenizerNormalizer.getNormalizer().getStopwords();
		String[] tokens = IStopwords.removeStopWords(stopwords, tnoutput.getTokens());
		if (isAunigram(tokens)) {
			return true;
		}
		
		if (startByAdigit(tokens)) {
			return(true);
		}
		
		if (lessThan7char(simpleTerm.getWord())) {
			return(true);
		}
		
		return false;
		
	}
	
	private boolean lessThan7char(String word) {
		return(word.length() < 7);
	}
	
	private boolean startByAdigit(String[] tokens) {
		String firstToken = tokens[0];
		if (firstToken.length() == 0) {
			return(true);
		}
		return(Character.isDigit(firstToken.charAt(0)));
	}

	private boolean isAunigram(String[] tokens) {
		if (tokens.length < 2) {
			return true;
		} else {
			return(false);
		}
	}
	
	
}
