package fr.erias.edsabbs.validators;

import fr.erias.abbsdetection.nounphrases.TF;

/**
 * This class is used to validate if a shortform has a context and to extract it
 * Ex: "avc ischemique", ischemique needs to be validated and extracted
 * @author cossins
 */
public interface IValidateContext {

	public boolean hasAcontext(TF tf);
	
	public String getContextWord(TF tf);
	
}
