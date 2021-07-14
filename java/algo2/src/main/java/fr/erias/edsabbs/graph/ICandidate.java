package fr.erias.edsabbs.graph;

import java.util.Collection;

import fr.erias.abbsdetection.nounphrases.TF;

public interface ICandidate {

	public void addContext(String contextWord, TF termWithContext, double idfContextWord);
	
	public String getLabel();
	
	public TF getTermWithContext(String contextWord);
	
	public Collection<TF> getTFs();
	
	public long getFreq();
	
	public double getNormValue();
	
	public double getNormValueTFIDF();
	
}
