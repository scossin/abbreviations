package fr.erias.edsabbs.graph;

import java.util.Set;

/**
 * This class represents the node "context" and is linked to Short and Long candidates that have this context
 * @author Sebastien Cossin
 */
public interface IOneContext {

	public void addShortCandidate(ICandidate shortCandidate);
	
	public void addLongCandidate(ICandidate longCandidate);
	
	public Set<ICandidate> getLongCandidates();
	
	public Set<ICandidate> getShortCandidates();
	
	public String getContextWord();
	
}
