package fr.erias.edsabbs.validators;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.IAMsystem.tokenizer.ITokenizer;
import fr.erias.abbsdetection.nounphrases.TF;
import fr.erias.abbsdetection.tokenizer.AbbTokenizer;
import fr.erias.edsabbs.stopwords.StopwordsAlgo2;

public class ContextValidator implements IValidateContext {

	private final static Logger logger = LoggerFactory.getLogger(ContextValidator.class);
	
	private IStopwords stopwords = StopwordsAlgo2.INSTANCE;
	
	private ITokenizer tokenizer = AbbTokenizer.tokenizer;

	@Override
	public boolean hasAcontext(TF tf) {
		String contextString = getContextWord(tf);
		if (contextString == null) { // null last token is a stopword
			logger.debug(tf.getWord() + " too many contextwords or only stopwords detected");
			return false;
		}
		return true;
	}
	
	@Override
	public String getContextWord(TF tf) {
		String[] tokens = tokenizer.tokenize(tf.getWord());
		String[] contextTokens = Arrays.copyOfRange(tokens, 1, tokens.length);
		String contextString = getContextWord(contextTokens);
		return(contextString);
	}
	
	/**
	 * Extract the first token that is not a stopword among an array of token
	 * @param contextTokens an array of token (the right context of a term)
	 * @return the first token (not a stopword) or null
	 */
	public String getContextWord(String [] contextTokens ) {
		// String contextWord = getFirstTokenNotAstopword(contextTokens); // deprecated => changed to last token
		String contextWord = getLastToken(contextTokens);
		return contextWord;
	}
	
	private String getLastToken(String [] contextTokens) {
		String lastToken = contextTokens[contextTokens.length - 1];
		if (this.stopwords.isStopWord(lastToken)) {
			return null;
		} else {
			return lastToken;
		}
	}
	
	@Deprecated
	private String getFirstTokenNotAstopword(String [] contextTokens) {
		// Take as context the first non-stopword token
		int firstPosNotStopword = getPosTokenNotStopword(contextTokens);
		if (firstPosNotStopword == -1){
			return null; // no token not a stopword
		}
		// subset the contextTokens
		String[] tokenNotStopword = Arrays.copyOfRange(contextTokens, firstPosNotStopword, firstPosNotStopword + 1); // +1: from-to; only one token extracted here at position "firstPosNotStopword"
		String contextString = ITokenizer.arrayToString(tokenNotStopword," ".charAt(0));
		return(contextString);
	}

	@Deprecated
	private int getPosTokenNotStopword(String[] tokens) {
		for (int i = 0; i<tokens.length;i++) {
			if (!this.stopwords.isStopWord(tokens[i])) {
				return(i);
			}
		}
		return(-1);
	}

}
