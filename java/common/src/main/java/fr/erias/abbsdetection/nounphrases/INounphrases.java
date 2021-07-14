package fr.erias.abbsdetection.nounphrases;

import java.util.Collection;

/**
 * This class gives access to nounphrases (term with a frequency)
 * 
 * @author Sebastien Cossin
 *
 */
public interface INounphrases extends IGetSimpleTerms {

	public Collection<TF> getTFs();
	
	public boolean containsTerm(String term); // check if noun phrases contains a specific term
	
	public TF getTF(String term);
	
	public double getIDF(String token);
	
}
