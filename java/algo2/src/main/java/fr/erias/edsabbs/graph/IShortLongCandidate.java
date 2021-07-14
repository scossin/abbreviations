package fr.erias.edsabbs.graph;

import java.util.Set;

/**
 * This class stores a pair <shortCandidate;longCandidate> and their common contexts
 * 
 * @author Sebastien Cossin
 */
public interface IShortLongCandidate {

	public void addCommonContext(ICommonContext commonContext);
		
	public Set<ICommonContext> getCommonContexts();
	
	public ICandidate getLongCandidate();

	public ICandidate getShortCandidate();
}
