package fr.erias.edsabbs.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.erias.abbsdetection.nounphrases.TF;
import fr.erias.edsabbs.validators.IValidatePair;
import fr.erias.edsabbs.validators.PairValidator;

public class CandidatePairs implements ICandidatePairs {
	
	private IStoreAllContextWords contextwords;
	
	private IValidatePair validatePair;
	
	private Map<String, Map<String,IShortLongCandidate>> mapShort2PairCandidates = new HashMap<String, Map<String,IShortLongCandidate>>();
	
	public Iterable<IShortLongCandidate> getShortLongCandidates(String shortForm) {
		return(this.mapShort2PairCandidates.get(shortForm).values());
	}
	
	public Iterable<String> getShortForms(){
		return(this.mapShort2PairCandidates.keySet());
	}
	
	public CandidatePairs(IStoreAllContextWords contextwords) {
		this.contextwords = contextwords;
		this.validatePair = new PairValidator();
		createPairs();
	}
	
	private void createPairs() {
		for (IOneContext context : contextwords.getContexts()) {
			String contextWord = context.getContextWord();
			Set<ICandidate> longCandidates = context.getLongCandidates();
			Set<ICandidate> shortCandidates = context.getShortCandidates();
			// O(N^2)
			for (ICandidate shortCandidate : shortCandidates) {
				for (ICandidate longCandidate: longCandidates) {
					if (this.validatePair.isAvalidPair(shortCandidate, longCandidate)) {
						createNewPair(shortCandidate, longCandidate, contextWord);
					}
				}
			}
		}
	}
	
	private void createNewPair(ICandidate shortCandidate, ICandidate longCandidate, String contextWord) {
		IShortLongCandidate shortLongCandidate = getOrCreateShortLongCandidate(shortCandidate, longCandidate);
		addCommonContext(shortLongCandidate, contextWord);
	}
	
	private void addCommonContext(IShortLongCandidate shortLongCandidate, String contextWord) {
		TF termShortForm = shortLongCandidate.getShortCandidate().getTermWithContext(contextWord);
		TF termLongForm = shortLongCandidate.getLongCandidate().getTermWithContext(contextWord);
		ICommonContext commonContext = new CommonContext(termLongForm, termShortForm, contextWord);
		shortLongCandidate.addCommonContext(commonContext);
	}
	
	
	private IShortLongCandidate getOrCreateShortLongCandidate(ICandidate shortCandidate, ICandidate longCandidate) {
		String shortForm = shortCandidate.getLabel();
		Map<String, IShortLongCandidate> mapId2ShortLongCandidate = getMapId2ShortLongCandidate(shortForm);
		String identifier = ShortLongCandidate.getIdentifier(shortCandidate, longCandidate);
		if (!mapId2ShortLongCandidate.containsKey(identifier)) {
			IShortLongCandidate shortLongCandidate = new ShortLongCandidate(shortCandidate, longCandidate);
			mapId2ShortLongCandidate.put(identifier, shortLongCandidate);
		} 
		IShortLongCandidate shortLongCandidate = this.mapShort2PairCandidates.get(shortForm).get(identifier);
		return shortLongCandidate;
	}
	
	
	private Map<String, IShortLongCandidate> getMapId2ShortLongCandidate(String shortForm){
		if (!this.mapShort2PairCandidates.containsKey(shortForm)) {
			Map<String, IShortLongCandidate> mapId2ShortLongCandidate = new HashMap<String, IShortLongCandidate>();
			this.mapShort2PairCandidates.put(shortForm, mapId2ShortLongCandidate);
		} 
		Map<String, IShortLongCandidate> mapId2ShortLongCandidate = this.mapShort2PairCandidates.get(shortForm);
		return(mapId2ShortLongCandidate);
	}
}
