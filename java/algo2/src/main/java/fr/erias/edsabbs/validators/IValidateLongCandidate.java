package fr.erias.edsabbs.validators;

import fr.erias.abbsdetection.nounphrases.TF;

public interface IValidateLongCandidate {

	public boolean isAlongCandidate(TF tf);
	
	public String getLongForm(TF tf);
	
	public String getContextWord(TF tf);
}
