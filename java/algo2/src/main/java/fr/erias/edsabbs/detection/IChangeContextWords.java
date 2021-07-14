package fr.erias.edsabbs.detection;

import fr.erias.edsabbs.graph.IStoreAllContextWords;

/**
 * The classes that implements this interface modify IStoreAllContextWords:
 * They add new Context (short or longCandidate)
 * 
 * @author Sebastien Cossin
 */
public interface IChangeContextWords {

	public IStoreAllContextWords getContextWords();
	
}
