package fr.erias.abbsdetection.nounphrases;

import java.util.HashMap;

import fr.erias.IAMsystem.tokenizer.ITokenizer;
import fr.erias.abbsdetection.tokenizer.AbbTokenizer;

public class CountTokens implements IcalculateIDF {

	private ITokenizer tokenizer = AbbTokenizer.tokenizer;
	
	private long total = 0; 
	
	private HashMap<String, Long> uniqueTokens = new HashMap<String, Long>();
	
	public CountTokens(INounphrases nounphrases) {
		for (TF tf : nounphrases.getTFs()) {
			addTF(tf);
		}
	}
	
	@Override
	public double getIDF(String token) {
		long freq;
		if (!uniqueTokens.containsKey(token)) {
			freq = 1;
		} else {
			freq = uniqueTokens.get(token);
		}
		return(Math.log(total/freq));
	}
	
	private void addTF(TF tf) {
		addTF(tf.getWord(), tf.getFreq());
	}
	
	private void addTF(String label, int freq) {
		for (String token : tokenizer.tokenize(label)) {
			if (!uniqueTokens.containsKey(token)) {
				uniqueTokens.put(token, 0L);
			}
			total = total + freq;
			uniqueTokens.put(token, uniqueTokens.get(token) + freq);
		}
	}
}

