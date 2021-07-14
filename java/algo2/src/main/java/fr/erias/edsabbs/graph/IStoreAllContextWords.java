package fr.erias.edsabbs.graph;

import java.util.Collection;

/**
 * This class is used to add, store and retrieve all IOneContext
 * @author Sebastien Cossin
 */
public interface IStoreAllContextWords {

	public boolean isAcontextWord(String contextword);
	
	public void addContextWordShort(String contextword, ICandidate shortCandidate);
	
	public void addContextWordLong(String contextword, ICandidate longCandidate);
	
	public Collection<IOneContext> getContexts();
}
