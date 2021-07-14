package fr.erias.edsabbs.validators;

import fr.erias.edsabbs.graph.ICandidate;

public interface IValidatePair {

	public boolean isAvalidPair(ICandidate shortCandidate, ICandidate longCandidate);
	
}
