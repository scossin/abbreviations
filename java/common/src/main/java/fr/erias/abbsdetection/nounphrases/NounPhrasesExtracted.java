package fr.erias.abbsdetection.nounphrases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class load all noun phrases in memory
 * 
 * @author Cossin Sebastien
 *
 */
public class NounPhrasesExtracted implements INounphrases {
	
	private HashMap<String,TF> mapLabel2TF = new HashMap<String,TF>(); 
	
	private Collection<TF> tfs;
	
	private IcalculateIDF countTokens;
	
	public NounPhrasesExtracted(File file) throws IOException {
		loadTF(file);
		calculateIDF();
	}

	private void calculateIDF() {
		this.countTokens = new CountTokens(this);
	}

	@Override
	public Iterable<? extends ISimpleTerm> getSimpleTerms() {
		return this.tfs;
	}

	@Override
	public double getIDF(String token) {
		return countTokens.getIDF(token);
	}

	@Override
	public Collection<TF> getTFs() {
		return this.tfs;
	}

	@Override
	public boolean containsTerm(String term) {
		return mapLabel2TF.containsKey(term);
	}
	
	/**
	 * Retrieve one noun phrase with its label
	 * Return a new one with frequency of 1 if not found
	 * @param token
	 * @return
	 */
	@Override
	public TF getTF(String token) {
		if (!mapLabel2TF.containsKey(token)) {
			return(new TF(token,1));
		}
		return (mapLabel2TF.get(token));
	}
	
	/**
	 * Load the CT file
	 * @param tabFile: lemmaTerm \t frequency
	 * @throws IOException
	 */
	private void loadTF(File tabFile) throws IOException {
		ArrayList<TF> tfs = new ArrayList<TF>();
		InputStream in = new FileInputStream(tabFile);
		String line = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		while ((line = br.readLine()) != null) {
			String[] columns = line.split("\t");
			if (columns.length != 2) {
				continue;
			}
			String lemmaTerm = columns[0];
			lemmaTerm = lemmaTerm.trim();
			String freq = columns[1];
			int count = Integer.parseInt(freq);
			TF tf = new TF(lemmaTerm, count);
			mapLabel2TF.put(lemmaTerm, tf);
			tfs.add(tf);
		}
		br.close();
		in.close();
		this.tfs = tfs;
	}
}
