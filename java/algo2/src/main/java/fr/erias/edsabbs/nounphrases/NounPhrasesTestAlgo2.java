package fr.erias.edsabbs.nounphrases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import fr.erias.abbsdetection.nounphrases.CountTokens;
import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.ISimpleTerm;
import fr.erias.abbsdetection.nounphrases.IcalculateIDF;
import fr.erias.abbsdetection.nounphrases.TF;

/**
 * Nounphrases used for testing
 * @author Sebastien Cossin
 */
public class NounPhrasesTestAlgo2 implements INounphrases {
	
	private Collection<TF> tfs;

	private HashMap<String, TF> mapLabel2TF = new HashMap<String, TF>();

	public IcalculateIDF countTokens;

	public NounPhrasesTestAlgo2() {
		this.tfs = new ArrayList<TF>();
		
		// avc
		TF avc = new TF("avc", 100);
		addTF(avc);
		
		TF avcLong = new TF("accident vasculaire cerebral", 100);
		addTF(avcLong);
		
		TF avcSylvien =new TF("avc sylvien", 20);
		addTF(avcSylvien);
		
		TF avcLongSylvien = new TF("accident vasculaire cerebral sylvien", 40);
		addTF(avcLongSylvien);
		
		TF avcLongIschemique = new TF("accident vasculaire cerebral ischemique", 40);
		addTF(avcLongIschemique);

		// IDM
		TF idm = new TF("idm",100);
		addTF(idm);
		
		TF idmIschemique = new TF("idm ischemique", 30);
		addTF(idmIschemique);
		
		TF idmLong = new TF("infarctus du myocarde", 30);
		addTF(idmLong);
		
		TF idmLongIschemique  = new TF("infarctus du myocarde ischemique", 30);
		addTF(idmLongIschemique);

		calculateIDF();
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
	public double getIDF(String token) {
		return this.countTokens.getIDF(token);
	}

	@Override
	public boolean containsTerm(String term) {
		return this.mapLabel2TF.containsKey(term);
	}

	@Override
	public TF getTF(String term) {
		return this.mapLabel2TF.get(term);
	}

	private void calculateIDF() {
		this.countTokens = new CountTokens(this);
	}

	private void addTF(TF tf) {
		this.tfs.add(tf);
		mapLabel2TF.put(tf.getWord(), tf);
	}
}
