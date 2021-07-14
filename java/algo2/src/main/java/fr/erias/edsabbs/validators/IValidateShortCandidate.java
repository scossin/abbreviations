package fr.erias.edsabbs.validators;

import fr.erias.abbsdetection.nounphrases.TF;

public interface IValidateShortCandidate {

	public boolean containsAshortForm(TF tf);
	
	public String getShortForm(TF tf);
	
}
