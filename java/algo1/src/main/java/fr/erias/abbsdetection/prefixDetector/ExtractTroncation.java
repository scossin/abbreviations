package fr.erias.abbsdetection.prefixDetector;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import fr.erias.IAMsystem.ct.CTcode;
import fr.erias.IAMsystem.detect.DetectOutput;
import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.IAMsystem.tokenizer.ITokenizer;
import fr.erias.IAMsystem.tokenizernormalizer.ITokenizerNormalizer;

public class ExtractTroncation {

	private TroncationDetector troncationDetector;
	
	public ExtractTroncation(TroncationDetector troncationDetector) throws IOException {
		this.troncationDetector = troncationDetector;
	}
	
	public HashSet<WordAbbCT> extractTroncation(String term){
		HashSet<WordAbbCT> troncations = new HashSet<WordAbbCT>();
		if (term.equals("")) {
			return(troncations);
		}
		try {
			DetectOutput detectOutput = troncationDetector.detect(term);
			ITokenizerNormalizer tokenizerNormalizer = troncationDetector.getTokenizerNormalizer();
			troncations = extractTroncation(tokenizerNormalizer, detectOutput.getCTcodes());
		} catch (Exception e) {
			System.out.println("Exception for term " + term);
			e.printStackTrace();
		}

		return troncations;
	}
	
	/**
	 * Convert set of {@link CTcode} to WordAbbreviation
	 * @param tokenizer to tokenize and compare tokens in text
	 * @param cts set of {@link CTcode}
	 * @return
	 */
	private HashSet<WordAbbCT> extractTroncation(ITokenizerNormalizer tokenizerNormalizer, Set<CTcode> cts){
		HashSet<WordAbbCT> troncations = new HashSet<WordAbbCT>();
		for (CTcode ct : cts) {
			HashSet<WordAbbCT> troncationsCT = detectTroncation(tokenizerNormalizer, ct);
			troncations.addAll(troncationsCT);
		}
		return (troncations);
	}
	
	private HashSet<WordAbbCT> detectTroncation(ITokenizerNormalizer tokenizerNormalizer, CTcode ct) {
		HashSet<WordAbbCT> troncationDetectedCT = new HashSet<WordAbbCT>();
		String[] longFormTokens = getNormalizedTokensLongForm(tokenizerNormalizer, ct);
		String[] shortFormTokens = getNormalizedTokensShortForm(tokenizerNormalizer, ct);

		// it's not the same if a stopword was remove
		if (!sameNumberOfTokens(longFormTokens, shortFormTokens)) {
			return troncationDetectedCT;
		}
		// an abbreviation must satisfy several rules below
		// if it passes, the abbreviations is added
		for (int i=0; i<longFormTokens.length;i++) {
			String tokenLong = longFormTokens[i];
			String tokenShort = shortFormTokens[i];
			
			if (tokenLong.equals(tokenShort)) {
				continue;
			}
			
			if (tokenLengthIsTooShort(tokenLong) || tokenLengthIsTooShort(tokenShort)) {
				continue;
			}
			
			if (startByAdigit(tokenShort)) {
				continue;
			}

			// we ignore one char because in most cases it's just the plural of the term
			if (thereIsOnlyOneCharDiff(tokenLong, tokenShort)) {
				continue;
			}
			String shortForm = tokenShort;
			String longForm = tokenLong;
			WordAbb wordAbb = new WordAbb(shortForm, longForm);
			WordAbbCT wordAbbCT = new WordAbbCT(wordAbb, ct);
			troncationDetectedCT.add(wordAbbCT);
		}
		return troncationDetectedCT;
	}
	
	private boolean startByAdigit(String tokenShort) {
		return(Character.isDigit(tokenShort.charAt(0)));
	}

	/**
	 * Convert a {@link CTcode} to WordAbbreviation
	 * @param tokenizer to tokenize and compare tokens in text
	 * @param ct a {@link CTcode}
	 * @return
	 */
	
	private boolean sameNumberOfTokens(String[] tokensTermino, String[] tokensText) {
		return tokensTermino.length == tokensText.length;
	}
	
	private String[] getNormalizedTokensLongForm(ITokenizerNormalizer tokenizerNormalizer, CTcode ct) {
		String normalizeTerm = ct.getTerm().getNormalizedLabel();
		ITokenizer tokenizer = tokenizerNormalizer.getTokenizer();
		String[] tokensTermino = tokenizer.tokenize(normalizeTerm);
		return (tokensTermino);
	}
	
	private String[] getNormalizedTokensShortForm(ITokenizerNormalizer tokenizerNormalizer, CTcode ct) {
		ITokenizer tokenizer = tokenizerNormalizer.getTokenizer();
		String[] tokensText = tokenizer.tokenize(ct.getCandidateTerm());
		IStopwords stopwords = tokenizerNormalizer.getNormalizer().getStopwords();
		tokensText = IStopwords.removeStopWords(stopwords, tokensText);
		return tokensText;
	}
	
	public boolean tokenLengthIsTooShort(String token) {
		if (token.length() < 3) {
			return true;
		}
		return(false);
	}

	public boolean thereIsOnlyOneCharDiff(String tokenLong, String tokenShort) {
		if (tokenShort.length() + 1 == tokenLong.length()) {
			return (true);
		}
		return(false);
	}
}
