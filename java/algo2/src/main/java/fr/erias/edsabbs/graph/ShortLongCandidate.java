package fr.erias.edsabbs.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a pair of short/long forms candidates that have a minimum of one context word in common
 * 
 * @author Sebastien Cossin
 *
 */
public class ShortLongCandidate implements IShortLongCandidate {

	public static String getIdentifier(ICandidate shortCandidate, ICandidate longCandidate) {
		return shortCandidate.getLabel() + ";" + longCandidate.getLabel();
	}
	
	private ICandidate shortCandidate;
	
	private ICandidate longCandidate;
	
	private Set<ICommonContext> commonContexts = new HashSet<ICommonContext>();
	
	/**
	 * Constructor
	 * Create a new pair short/long pair candidate
	 * @param longCandidate ex: "accident vasculaire cerebral"
	 * @param shortForm ex: "avc"
	 */
	public ShortLongCandidate(ICandidate shortCandidate, ICandidate longCandidate) {
		this.longCandidate = longCandidate;
		this.shortCandidate = shortCandidate;
	}
	
	@Override
	public void addCommonContext(ICommonContext commonContext) {
		commonContexts.add(commonContext);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(shortCandidate.getLabel() + "\t" + longCandidate.getLabel() + "\n");
		for (ICommonContext commonContext : commonContexts) {
			sb.append("\t" + commonContext.getTermShortForm().getWord() + " == " + commonContext.getTermLongForm().getWord() + "\n");
		}
		return(sb.toString());
	}
	
	@Override
	public Set<ICommonContext> getCommonContexts() {
		return commonContexts;
	}
	
	@Override
	public ICandidate getLongCandidate() {
		return longCandidate;
	}

	@Override
	public ICandidate getShortCandidate() {
		return shortCandidate;
	}

}
