package fr.erias.edsabbs.detection;

import java.util.HashMap;

import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.TF;
import fr.erias.edsabbs.graph.Candidate;
import fr.erias.edsabbs.graph.ICandidate;
import fr.erias.edsabbs.graph.IStoreAllContextWords;
import fr.erias.edsabbs.graph.StoreContextWords;
import fr.erias.edsabbs.validators.ContextValidator;
import fr.erias.edsabbs.validators.IValidateContext;
import fr.erias.edsabbs.validators.IValidateShortCandidate;
import fr.erias.edsabbs.validators.ShortCandidateValidator;

public class DetectShortForms implements IChangeContextWords {

	private IValidateShortCandidate validator;
	
	private IValidateContext contextValidator;
	
	private IStoreAllContextWords contextWords;
	
	private HashMap<String, ICandidate> mapContext2shortCandidates = new HashMap<String, ICandidate>();

	private INounphrases nounphrases;

	@Override
	public IStoreAllContextWords getContextWords() {
		return(this.contextWords);
	}
	
	public DetectShortForms(INounphrases nounphrases) {
		this.nounphrases = nounphrases;
		this.validator = new ShortCandidateValidator(nounphrases);
		this.contextValidator = new ContextValidator();
		this.contextWords = new StoreContextWords();
		for (TF tf : nounphrases.getTFs()) {
			detectShortForm(tf);
		}
	}
	
	public void detectShortForm(TF tf) {
		if (!validator.containsAshortForm(tf)) {
			return;
		}
		String shortForm = validator.getShortForm(tf);
		
		if (!contextValidator.hasAcontext(tf)) {
			return;
		}
		
		String contextword = contextValidator.getContextWord(tf);
		
		ICandidate shortCandidate = getShortCandidateOrcreateIfNotExists(shortForm);
		double idfContextWord = nounphrases.getIDF(contextword);
		shortCandidate.addContext(contextword, tf, idfContextWord);
		contextWords.addContextWordShort(contextword, shortCandidate);
	}

	
	private ICandidate getShortCandidateOrcreateIfNotExists(String shortForm) {
		if (!this.mapContext2shortCandidates.containsKey(shortForm)) {
			createShortCandidate(shortForm);
		}
		ICandidate shortCandidate = this.mapContext2shortCandidates.get(shortForm); 
		return shortCandidate;
	}
	
	private void createShortCandidate(String shortForm) {
		ICandidate shortCandidate = new Candidate(shortForm);
		mapContext2shortCandidates.put(shortForm, shortCandidate);
	}
}
