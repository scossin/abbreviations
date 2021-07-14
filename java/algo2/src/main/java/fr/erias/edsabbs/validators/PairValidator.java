package fr.erias.edsabbs.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.IAMsystem.tokenizer.ITokenizer;
import fr.erias.abbsdetection.tokenizer.AbbTokenizer;
import fr.erias.edsabbs.graph.ICandidate;
import fr.erias.edsabbs.stopwords.StopwordsAlgo2;

public class PairValidator implements IValidatePair {

	private IStopwords stopwords = StopwordsAlgo2.INSTANCE;
	
	private ITokenizer tokenizer = AbbTokenizer.tokenizer;
	
	private Map<String, Set<String>> mapShort2bannedLongForms = new HashMap<String, Set<String>>();

	private Set<String> getBannedLongForms(String shortForm) {
		createNewSetIfNotExists(shortForm);
		return(mapShort2bannedLongForms.get(shortForm));
	}
	
	private void createNewSetIfNotExists(String shortForm) {
		if (!mapShort2bannedLongForms.containsKey(shortForm)) {
			Set<String> bannedLongForms = new HashSet<String>();
			mapShort2bannedLongForms.put(shortForm, bannedLongForms);
		}
	}
	
	/**
	 * Add candidate longForm for this shortForm. LongForm must share the same contextWord
	 * @param longCandidate "accident vasculaire cerebral"
	 * @param termLongForm "accident vasculaire cerebral sylvien"
	 * @param contextWord "sylvien"
	 */
	public boolean isAvalidPair(ICandidate shortCandidate, ICandidate longCandidate) {
		String shortForm = shortCandidate.getLabel();
		String longForm = longCandidate.getLabel();
		
		Set<String> bannedLongForms = getBannedLongForms(shortForm);
		if (bannedLongForms.contains(longForm)) {
			return false;
		}
		
		// first thing to test since it's very quick to compare
		if (!sameFirstLetter(longForm, shortForm)) {
			bannedLongForms.add(longForm);
			return false;
		}
		
		if (shortFormInLongForm(longForm, shortForm)) {
			bannedLongForms.add(longForm);
			return false;
		}
		
		if (!satisfySchwartzRules(longForm, shortForm)) {
			bannedLongForms.add(longForm);
			return false;
		}
		
		return true;
		
	}
	
	private boolean sameFirstLetter(String longForm, String shortForm) {
		return(longForm.charAt(0) == shortForm.charAt(0));
	}

	private boolean satisfySchwartzRules(String longForm, String shortForm) {
		return allLettersFoundInRightOrder(longForm, shortForm) &&  // rule 1 and 2
				eachLongFormFirstCharInShortForm(longForm, shortForm); // rule 3)
	}

	private boolean allLettersFoundInRightOrder(String longForm, String shortForm) {
		int y = 0;
		char shortCar = shortForm.charAt(y);
		for (int i = 0; i < longForm.length(); i++){
			char c = longForm.charAt(i);     
			if (shortCar == c) {
				y = y + 1;
				if (y == shortForm.length()) { // all letters were checked
					return(true);
				} else {
					shortCar = shortForm.charAt(y);
				}
			}
		} 
		return(false); // not all letters of short form were found in long form
	}

	@Deprecated
	private boolean eachLongFormTokenContainsAtLeastOneCharacter(String longForm, String shortForm) {
		HashSet<Character> setChars = new HashSet<Character>();
		String shortFormString = shortForm;
		for (int i = 0; i < shortFormString.length(); i++){
			char c = shortFormString.charAt(i);
			setChars.add(c);
		}
		String[] tokens = this.tokenizer.tokenize(longForm);
		for (String token : tokens) {
			if (stopwords.isStopWord(token)) { // ignore stopword
				continue;
			}
			Boolean charFound = false;
			for (int i = 0; i < token.length(); i++){
				char c = token.charAt(i);
				if (setChars.contains(c)) { // a character is found in the longForm, check next token
					charFound = true;
				}
			}
			// no char found in this token: return false
			if (!charFound) {
				return(false);
			}
		}
		return(true); // all tokens of longForm checked and they all contain at least on character of the shortForm
	}

	private boolean eachLongFormFirstCharInShortForm(String longForm, String shortForm) {
		ArrayList<Character> setChars = new ArrayList<Character>();
		String shortFormString = shortForm;
		for (int i = 0; i < shortFormString.length(); i++){
			char c = shortFormString.charAt(i);
			setChars.add(c);
		}
		String[] tokens = this.tokenizer.tokenize(longForm);
		for (String token : tokens) {
			if (stopwords.isStopWord(token)) { // ignore stopword
				continue;
			}
			char c = token.charAt(0);
			int indexPosition = setChars.indexOf(c);
			if (indexPosition != -1) { // a character is found in the longForm, check next token
				setChars.remove(indexPosition); // the character was used
				continue;
			} else {
				return(false);
			}
			// no char found in this token: return false
		}
		return(true); // all tokens of longForm checked and they all contain at least on character of the shortForm
	}

	private boolean shortFormInLongForm(String longForm, String shortForm) {
		for (String token : tokenizer.tokenize(longForm)) {
			if (shortForm.equals(token)) {
				return(true);
			}
		}
		return(false);
	}

}
