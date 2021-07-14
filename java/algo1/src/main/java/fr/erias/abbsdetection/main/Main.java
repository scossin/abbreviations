package fr.erias.abbsdetection.main;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.erias.IAMsystem.terminology.Term;
import fr.erias.IAMsystem.terminology.Terminology;
import fr.erias.abbsdetection.nounphrases.NounPhrasesExtracted;
import fr.erias.abbsdetection.nounphrases.Terminology4detection;
import fr.erias.abbsdetection.prefixDetector.ExtractTroncation;
import fr.erias.abbsdetection.prefixDetector.TroncationDetector;
import fr.erias.abbsdetection.prefixDetector.WordAbbCT;
import fr.erias.edsabb.writer.IWriteOutput;
import fr.erias.edsabb.writer.WriteOutput;

public class Main {
	
	private final static Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException {
		String nounphrasesFilename = args[0];
		String outputFilename = args[1];
		File outputFile = new File(outputFilename);
		IWriteOutput outputWriter = new WriteOutput(outputFile);
		File nounphraseFile = new File(nounphrasesFilename);
		
		logger.info("loading nounphrases...");
		NounPhrasesExtracted nounphrases = new NounPhrasesExtracted(nounphraseFile);
		logger.info(nounphrases.getTFs().size() + " nounphrases loaded");
		
		Terminology4detection terminology4detect = new Terminology4detection(nounphrases);
		Terminology terminology = terminology4detect.getTerminology();
		int nounphrasesSize = terminology.getTerms().size();
		TroncationDetector detector = new TroncationDetector(terminology);
		ExtractTroncation extractor = new ExtractTroncation(detector);
		
		outputWriter.write(WordAbbCT.getHeader() + "\n");
		
		int count = 0;
		logger.info("starting detection...");
		for (Term term : terminology.getTerms()) {
			count = count + 1;
			logProgression(count, nounphrasesSize);
			HashSet<WordAbbCT> troncationDetected = extractor.extractTroncation(term.getLabel());
			for (WordAbbCT wordAbb : troncationDetected) {
				String newLine = wordAbb.getStringOutput(nounphrases) + "\n";
				outputWriter.write(newLine);
			}
		}
	}
	
	private static void logProgression(int count, int nounphrasesSize) {
		long value = Math.round(100 * (double) count/ (double) nounphrasesSize);
		if (value > previousRoundedValue) {
			logger.info(value + "%");
			previousRoundedValue = value;
		}
	}
	
	private static long previousRoundedValue = 0;
	
}
