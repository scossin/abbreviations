package fr.erias.edsabbs.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StoreContextWords implements IStoreAllContextWords {

	private Map<String, IOneContext> contexts = new HashMap<String, IOneContext>();
	
	public Collection<IOneContext> getContexts() {
		return contexts.values();
	}

	@Override
	public boolean isAcontextWord(String contextword) {
		return this.contexts.containsKey(contextword);
	}
	
	public void createNewContext(String contextword) {
		IOneContext context = new Context(contextword);
		contexts.put(contextword, context);
	}
	
	@Override
	public void addContextWordShort(String contextword, ICandidate shortCandidate) {
		if (!isAcontextWord(contextword)) {
			createNewContext(contextword);
		}
		contexts.get(contextword).addShortCandidate(shortCandidate);
	}

	@Override
	public void addContextWordLong(String contextword, ICandidate longCandidate) {
		if (!isAcontextWord(contextword)) {
			createNewContext(contextword);
		}
		contexts.get(contextword).addLongCandidate(longCandidate);
	}
}
