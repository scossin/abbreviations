package fr.erias.edsabbs.validators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.erias.IAMsystem.tokenizer.ITokenizer;
import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.TF;
import fr.erias.abbsdetection.tokenizer.AbbTokenizer;

public class ShortCandidateValidator implements IValidateShortCandidate {
	
	private final int minShortFormFrequency = 10;
	
	private final static Logger logger = LoggerFactory.getLogger(ShortCandidateValidator.class);
	
	private ITokenizer tokenizer = AbbTokenizer.tokenizer;

	private INounphrases nounphrases;
	
	public ShortCandidateValidator(INounphrases nounphrases) {
		this.nounphrases = nounphrases;
	}
	
	public String getShortForm(TF tf) {
		String[] tokens = tokenizer.tokenize(tf.getWord());
		String firstToken = tokens[0];
		return(firstToken);
	}
	
	public boolean containsAshortForm(TF tf) {
		String[] tokens = tokenizer.tokenize(tf.getWord());
		
		if (onlyOneToken(tokens, tf)) {
			return false; // no context if only one token 
		}
		
		String shortForm = getShortForm(tf);
		
		if (firstTokenBeginsByAdigit(shortForm, tf)) {
			return false; // must not begin by a digit
		}
		
		if (shortForm.length() > 4) {
			logger.debug(tf.getWord() + " begins with a word with length > 4: next");
			return false; // an abbreviation must have less than 5 characters
		}
		
		if (!isShortFormAlemmaTerm(shortForm, nounphrases)) {
			logger.debug(shortForm + " this shortForm is not a lemmaTerm by itself");
			return false;
		}
		TF lemmaTerm = nounphrases.getTF(shortForm);
		if (lemmaTerm.getFreq() < minShortFormFrequency) {
			logger.debug(shortForm + " the frequency of the shortForm is less than " + minShortFormFrequency + "next");
			return false;
		}
		return true;
	}
	
	private boolean onlyOneToken(String[] tokens, TF tf) {
		if (tokens.length < 2) {
			logger.debug(tf.getWord() + " has only one token: next");
			return true;
		} else {
			return false;
		}
	}
	
	private boolean firstTokenBeginsByAdigit(String token, TF tf) {
		if (Character.isDigit( token.charAt(0))) {
			logger.debug(tf.getWord() + " begins with a digit: next");
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isShortFormAlemmaTerm(String firstToken, INounphrases nounphrases) {
		return nounphrases.containsTerm(firstToken);
	}
}
