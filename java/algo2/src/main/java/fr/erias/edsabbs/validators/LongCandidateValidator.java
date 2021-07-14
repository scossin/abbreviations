package fr.erias.edsabbs.validators;

import java.util.Arrays;

import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.IAMsystem.tokenizer.ITokenizer;
import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.TF;
import fr.erias.abbsdetection.tokenizer.AbbTokenizer;
import fr.erias.edsabbs.graph.IStoreAllContextWords;
import fr.erias.edsabbs.stopwords.StopwordsAlgo2;

public class LongCandidateValidator implements IValidateLongCandidate {

	private ITokenizer tokenizer = AbbTokenizer.tokenizer;
	
	public static final int minLongFormFrequency = 10;
	
	private INounphrases nounphrases;

	private IStopwords stopwords = StopwordsAlgo2.INSTANCE;

	private IStoreAllContextWords contextWords;

	public LongCandidateValidator(INounphrases nounphrases, IStoreAllContextWords contextWords) {
		this.nounphrases = nounphrases;
		this.contextWords = contextWords;
	}


	@Override
	public boolean isAlongCandidate(TF tf) {
		String[] tokens = tokenizer.tokenize(tf.getWord());

		if (tokens.length < 2) {
			return false;
		}

		String lastToken = tokens[tokens.length-1];

		if (this.stopwords.isStopWord(lastToken)) {
			return false;
		}

		// check if it's a known context
		if (!contextWords.isAcontextWord(lastToken)) {
			return false;
		}
		
		String longFormString = getLongForm(tf);
		// the longForm must exist alone (without context) to be a longForm candidate
		if (!nounphrases.containsTerm(longFormString)) {
			return false;
		}
		
		int longFormFrequency = nounphrases.getTF(longFormString).getFreq();
		if (longFormFrequency < minLongFormFrequency) { // frequency at least 10
			return false;
		}

		return true;

	}
	
	// TODO: same context word as short candidate
	@Override
	public String getContextWord(TF tf) {
		String[] tokens = tokenizer.tokenize(tf.getWord());
		String lastToken = tokens[tokens.length-1];
		return(lastToken);
	}

	@Override
	public String getLongForm(TF tf) {
		String[] tokens = tokenizer.tokenize(tf.getWord());
		// previous tokens = longForm
		String[] tokensWithoutLastOne = Arrays.copyOf(tokens, tokens.length - 1);
		// remove stopword
		String longFormString = ITokenizer.arrayToString(tokensWithoutLastOne," ".charAt(0));
		return longFormString;
	}

}
