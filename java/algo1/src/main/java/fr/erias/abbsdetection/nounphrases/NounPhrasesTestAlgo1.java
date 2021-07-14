package fr.erias.abbsdetection.nounphrases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class NounPhrasesTestAlgo1 implements INounphrases {

	private Collection<TF> tfs;
	
	private HashMap<String, TF> mapLabel2TF = new HashMap<String, TF>();
	
	public NounPhrasesTestAlgo1() {
		this.tfs = new ArrayList<TF>();
		
		TF tf1 = new TF("accident vasculaire cerebral", 50);
		tfs.add(tf1);
		
		TF tf2 = new TF("infarctus du myocarde", 30);
		tfs.add(tf2);
		
		TF tf_unigram = new TF("avc", 30);
		tfs.add(tf_unigram);
		
		TF abces = new TF("abces anal operes", 30);
		tfs.add(abces);
		
		buildMapLabel2TF();
	}
	
	@Override
	public Collection<TF> getTFs() {
		return(this.tfs);
	}

	@Override
	public Iterable<? extends ISimpleTerm> getSimpleTerms() {
		return(this.tfs);
	}

	@Override
	public boolean containsTerm(String term) {
		return this.mapLabel2TF.containsKey(term);
	}

	@Override
	public TF getTF(String term) {
		return this.mapLabel2TF.get(term);
	}

	@Override
	public double getIDF(String token) {
		throw new NullPointerException("IDF is not used in algo1");
	}
	
	
	private void buildMapLabel2TF() {
		for (TF tf : tfs) {
			mapLabel2TF.put(tf.getWord(), tf);
		}
	}

}
