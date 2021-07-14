package fr.erias.edsabbs.countFreqAbbs;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

import fr.erias.IAMsystem.detect.DetectOutput;
import fr.erias.IAMsystem.normalizer.INormalizer;
import fr.erias.IAMsystem.normalizer.Normalizer;
import fr.erias.IAMsystem.tokenizer.TokenizerWhiteSpace;
import fr.erias.abbsdetection.config.Config;
import fr.erias.edsabbs.eds.Observation;
import fr.erias.edsabbs.eds.RetrieveObservationOracleProvider;

public class CountTokens {

	private INormalizer normalizer;
	private ITokenizer tokenizer = AbbTok;
	
	public CountTokens() {
		this.normalizer = new Normalizer();
		this.tokenizer = new TokenizerWhiteSpace();
	}
	
	private HashMap<String, Long> uniqueTokens = new HashMap<String, Long>();
	
	public void addDoc(String doc) {
		String normalizedDoc = normalizer.getNormalizedSentence(doc);
		for (String token : tokenizer.tokenize(normalizedDoc)) {
			if (!uniqueTokens.containsKey(token)) {
				uniqueTokens.put(token, 0L);
			}
			uniqueTokens.put(token, uniqueTokens.get(token) + 1);
		}
	}
	
	public void addDoc(String doc, int freq) {
		String normalizedDoc = normalizer.getNormalizedSentence(doc);
		for (String token : tokenizer.tokenize(normalizedDoc)) {
			if (!uniqueTokens.containsKey(token)) {
				uniqueTokens.put(token, 0L);
			}
			uniqueTokens.put(token, uniqueTokens.get(token) + freq);
		}
	}
	
	public HashMap<String, Long>  getUniqueTokens() {
		return(this.uniqueTokens);
	}
	
	public void printNumbers() {
		System.out.println("Number of unique tokens: " + this.uniqueTokens.size());
		long total = 0L;
		for (Long value : uniqueTokens.values()) {
			total = total+ value;
		}
		System.out.println("Number of tokens: " + total);
	}
}

