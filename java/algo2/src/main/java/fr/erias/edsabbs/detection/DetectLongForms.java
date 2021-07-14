package fr.erias.edsabbs.detection;

import java.io.IOException;
import java.util.HashMap;

import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.TF;
import fr.erias.edsabbs.graph.Candidate;
import fr.erias.edsabbs.graph.ICandidate;
import fr.erias.edsabbs.graph.IStoreAllContextWords;
import fr.erias.edsabbs.validators.IValidateLongCandidate;
import fr.erias.edsabbs.validators.LongCandidateValidator;

public class DetectLongForms implements IChangeContextWords {

	private IValidateLongCandidate validator; 
	
	private IStoreAllContextWords contextWords;
	
	@Override
	public IStoreAllContextWords getContextWords() {
		return(contextWords);
	}
	
	/**
	 * Store all the shortCandidate (we need to access it to add context)
	 */
	private HashMap<String,ICandidate> longCandidates = new HashMap<String,ICandidate>();

	private INounphrases nounphrases;

	public HashMap<String,ICandidate> getLongCandidates() {
		return(this.longCandidates);
	}
	
	public DetectLongForms(INounphrases nounphrases, IStoreAllContextWords contextWords) {
		this.nounphrases = nounphrases;
		this.contextWords = contextWords;
		this.validator = new LongCandidateValidator(nounphrases, contextWords);
		for (TF tf : nounphrases.getTFs()) {
			detectLongForm(tf);
		}
	}
	
	private void detectLongForm(TF tf) {
		if (!this.validator.isAlongCandidate(tf)) {
			return;
		}
		String longForm = validator.getLongForm(tf);
		
		// check if long form already exists:
		ICandidate longCandidate = getLongCandidate(longForm);

		String contextword = validator.getContextWord(tf);
		double idfContextWord = nounphrases.getIDF(contextword);
		longCandidate.addContext(contextword, tf, idfContextWord);
		this.contextWords.addContextWordLong(contextword, longCandidate);
	}
	
	/**
	 * Detect the longForm of a term
	 * @throws IOException 
	 */
	
	private ICandidate getLongCandidate(String longForm) {
		if (!this.longCandidates.containsKey(longForm)) { 
			createNewLongCandidate(longForm);
		}
		return this.longCandidates.get(longForm);
	}
	
	private void createNewLongCandidate(String longForm) {
		ICandidate longCandidate = new Candidate(longForm);
		this.longCandidates.put(longForm, longCandidate);
	}


	/**
	 * The objective of this function is to count the frequency of longforms with their context
	 * Add to longForm a new context word if exists
	 * @param tfs
	 */
//	public void countLongForms(List<TF> tfs) {
//		logger.info("********** counting long forms ******************");
//		logger.info("number of longForms: " + this.longCandidates.values().size());
//
//		// order the longForms
//		TreeSet<LongCandidate> longFormsOrdered = new TreeSet<LongCandidate>();
//		Iterator<LongCandidate> iterLongFormUnordered = this.longCandidates.values().iterator();
//		while(iterLongFormUnordered.hasNext()) {
//			LongCandidate longForm = iterLongFormUnordered.next();
//			longFormsOrdered.add(longForm);
//		}
//
//		// iter over an ordered longForm
//		Iterator<LongCandidate> iterLongFormOrdered = longFormsOrdered.iterator();
//		int count = 0;
//		LongCandidate longForm = iterLongFormOrdered.next(); // first longForm
//
//		// at each step we compare alphabetically the longForm and the lemmaTerm
//		for (int i = 0; i<tfs.size(); i++) {
//			TF tf = tfs.get(i);
//			int comparison = tf.getWord().compareTo(longForm.getWord());
//			if (comparison == 0) { // same word. Ex: "accident vasculaire cerebral" and "accident vasculaire cerebral". We want longForm + context, not longForm alone
//				continue;
//			}
//
//			if (comparison > 0) { // tf is alphabetically below 
//				if (tf.getWord().startsWith(longForm.getWord())) { // we check if TF startswith longForm. Ex: "accident vasculaire cerebral sylvien", "accident vasculaire cerebral";
//					// if true, we add to the longForm this lemmaTerm (longForm + context);
//					LemmaTerm termLongForm = new LemmaTerm(tf.getWord(), tf.getFreq());
//					longForm.addLemmaTerm(termLongForm);
//					count = count +1; // count the number of times we add 
//					continue;
//				} else { // we need to change the longForm, go next;
//					if (!iterLongFormOrdered.hasNext()) { // check first if it exists
//						break;
//					} 
//					i = i - count; // TF goes up to start to check again if it begins by the longForm
//					count = 0; // reset to 0
//					longForm = iterLongFormOrdered.next(); // next longForm 
//					continue;
//				}
//				// longForm is below
//			} else { // comparison <0: tf is alphabetically above, need to go down:next
//				continue;
//			}
//		}
//	}
}
