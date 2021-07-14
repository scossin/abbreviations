package fr.erias.edsabbs.troncation;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.HashSet;

import org.junit.Test;

import fr.erias.IAMsystem.terminology.Terminology;
import fr.erias.IAMsystem.tokenizernormalizer.ITokenizerNormalizer;
import fr.erias.IAMsystem.tokenizernormalizer.TokenizerNormalizer;
import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.NounPhrasesTestAlgo1;
import fr.erias.abbsdetection.nounphrases.Terminology4detection;
import fr.erias.abbsdetection.prefixDetector.ExtractTroncation;
import fr.erias.abbsdetection.prefixDetector.TroncationDetector;
import fr.erias.abbsdetection.prefixDetector.WordAbbCT;

public class TroncationTest {
	
	@Test
	public void removeUnigramTest() throws IOException {
		INounphrases nounphrases = new NounPhrasesTestAlgo1();
		Terminology4detection termino4detect = new Terminology4detection(nounphrases);
		Terminology terminology = termino4detect.getTerminology();
		int number_of_terms = terminology.getTerms().size();
		assertEquals(number_of_terms, 3);
	}
	
	@Test
	public void troncationDetectionTest() throws IOException {
		INounphrases nounphrases = new NounPhrasesTestAlgo1();
		Terminology4detection termino4detect = new Terminology4detection(nounphrases);
		Terminology terminology = termino4detect.getTerminology();
		TroncationDetector detector = new TroncationDetector(terminology);
		ExtractTroncation extractor = new ExtractTroncation(detector);
		String term = "accident vasc cerebral";
		HashSet<WordAbbCT> detectAbbs = extractor.extractTroncation(term);
		assertEquals(detectAbbs.size(), 1);
		for (WordAbbCT wordAbb : detectAbbs) {
			assertTrue(wordAbb.getWordAbb().getShortForm().equals("vasc"));
			assertTrue(wordAbb.getWordAbb().getLongForm().equals("vasculaire"));
		}
	}
	
	@Test
	public void troncationNotDetectedTest() throws IOException {
		INounphrases nounphrases = new NounPhrasesTestAlgo1();
		Terminology4detection termino4detect = new Terminology4detection(nounphrases);
		Terminology terminology = termino4detect.getTerminology();
		TroncationDetector detector = new TroncationDetector(terminology);
		ExtractTroncation extractor = new ExtractTroncation(detector);
		String term = "abces anal opere"; // only one char diff with operes
		HashSet<WordAbbCT> detectAbbs = extractor.extractTroncation(term);
		assertEquals(detectAbbs.size(), 0);
	}
}
