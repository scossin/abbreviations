package fr.erias.edsabbs.graph;

import fr.erias.abbsdetection.nounphrases.TF;


/**
 * This class stores two noun phrases, from a short and long candidate, that have the same context
 * Ex: "avc sylvien" and "accident vasculaire cerebral sylvien"
 * @author Sebastien Cossin
 *
 */
public interface ICommonContext {

	public TF getTermShortForm();
	
	public TF getTermLongForm();

	public String getContextWord();
}
