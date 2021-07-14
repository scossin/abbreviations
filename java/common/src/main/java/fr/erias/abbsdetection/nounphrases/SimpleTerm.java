package fr.erias.abbsdetection.nounphrases;

public class SimpleTerm implements ISimpleTerm {

	protected String label;
	
	public SimpleTerm(String label) {
		this.label = label;
	}
	
	@Override
	public String getWord() {
		return(this.label);
	}
}
