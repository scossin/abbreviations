package fr.erias.edsabbs.graph;

import java.util.Collection;
import java.util.HashMap;

import fr.erias.abbsdetection.nounphrases.TF;

public class Candidate implements ICandidate {

	private String longForm;
	private long total = 0;
	private long totalSquared = 0;
	private double totalSquaredTFIDF = 0;

	private HashMap<String,TF> mapContext2LongFormWithContext = new HashMap<String,TF>();

	public Candidate(String longForm) {
		this.longForm = longForm;
	}

	public String getLongForm() {
		return(this.longForm);
	}

	@Override
	public TF getTermWithContext(String contextWord) {
		return mapContext2LongFormWithContext.get(contextWord);
	}

	@Override
	public void addContext(String contextWord, TF tf, double idfContextWord) {
		mapContext2LongFormWithContext.put(contextWord, tf);
		updateMetrics(tf, idfContextWord);
	}

	@Override
	public String getLabel() {
		return this.longForm;
	}

	@Override
	public Collection<TF> getTFs() {
		return mapContext2LongFormWithContext.values();
	}

	@Override
	public long getFreq() {
		return(this.total);
	}

	private void updateMetrics(TF tf, double idfContextWord) {
		this.total = this.total + tf.getFreq();
		this.totalSquared = this.totalSquared + tf.getFreq() * tf.getFreq();
		this.totalSquaredTFIDF = this.totalSquaredTFIDF + tf.getFreq() * tf.getFreq() *
				idfContextWord * idfContextWord;
	}

	@Override
	public double getNormValue() {
		return(Math.sqrt(totalSquared));
	}

	@Override
	public double getNormValueTFIDF() {
		return(Math.sqrt(totalSquaredTFIDF));
	}
}
