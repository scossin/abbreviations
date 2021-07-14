package fr.erias.edsabbs.graph;

public interface ICandidatePairs {
	
	public Iterable<IShortLongCandidate> getShortLongCandidates(String shortForm);
	
	public Iterable<String> getShortForms();
	
}
