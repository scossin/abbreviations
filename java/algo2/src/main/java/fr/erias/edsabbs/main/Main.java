package fr.erias.edsabbs.main;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.NounPhrasesExtracted;
import fr.erias.edsabb.writer.IWriteOutput;
import fr.erias.edsabb.writer.WriteOutput;
import fr.erias.edsabbs.detection.DetectLongForms;
import fr.erias.edsabbs.detection.DetectShortForms;
import fr.erias.edsabbs.graph.CandidatePairs;
import fr.erias.edsabbs.graph.ICandidatePairs;
import fr.erias.edsabbs.graph.IStoreAllContextWords;
import fr.erias.edsabbs.metrics.ComputeMetrics;

public class Main {

	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {
		String filename = args[0];
		File nounphrasesFile = new File(filename);
		
		String outputFileName = args[1];
		File outputFile = new File(outputFileName);
		
		INounphrases nounphrases = new NounPhrasesExtracted(nounphrasesFile);

		logger.info("********** Detecting shortForm ******************");
		DetectShortForms detectShort = new DetectShortForms(nounphrases);
		IStoreAllContextWords contextWords = detectShort.getContextWords();

		logger.info("********** Detecting longForm ******************");
		DetectLongForms detectLongForms = new DetectLongForms(nounphrases, contextWords);
		IStoreAllContextWords updatedContextWords = detectLongForms.getContextWords();
		
		logger.info("********** Detecting pairs of short-long forms ******************");
		ICandidatePairs candidatePairs = new CandidatePairs(updatedContextWords);

		IWriteOutput writeOutput = new WriteOutput(outputFile);
		
		logger.info("********** Computing metrics ******************");
		ComputeMetrics computeMetrics = new ComputeMetrics(writeOutput);
		computeMetrics.compute(candidatePairs, nounphrases);
	}
}
