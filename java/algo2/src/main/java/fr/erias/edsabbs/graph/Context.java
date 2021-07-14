package fr.erias.edsabbs.graph;

import java.util.HashSet;
import java.util.Set;


public class Context implements IOneContext {
	
	private Set<ICandidate> shortCandidates = new HashSet<ICandidate>(); 
	
	private Set<ICandidate> longCandidates = new HashSet<ICandidate>(); 
	
	private String contextWord;
	
	public Set<ICandidate> getLongCandidates() {
		return longCandidates;
	}
	
	public Set<ICandidate> getShortCandidates() {
		return shortCandidates;
	}
	
	public Context(String contextWord) {
		this.contextWord = contextWord;
	}
	
	public String getContextWord() {
		return(this.contextWord);
	}
	
	@Override
	public void addShortCandidate(ICandidate shortCandidate) {
		this.shortCandidates.add(shortCandidate);
	}

	@Override
	public void addLongCandidate(ICandidate longCandidate) {
		this.longCandidates.add(longCandidate);		
	}
	
	@Override
    public int hashCode() {
		return(this.contextWord.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		// If the object is compared with itself then return true  
        if (o == this) {
            return true;
        }
  
        if (!(o instanceof Context)) {
            return false;
        }
        Context c = (Context) o;
		return(c.getContextWord().equals(this.contextWord));
	}
}
	
