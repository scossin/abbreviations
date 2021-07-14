package fr.erias.edsabbs.detection;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.edsabb.writer.IWriteOutput;
import fr.erias.edsabb.writer.WriteOutputTest;
import fr.erias.edsabbs.graph.CandidatePairs;
import fr.erias.edsabbs.graph.ICandidate;
import fr.erias.edsabbs.graph.ICandidatePairs;
import fr.erias.edsabbs.graph.IOneContext;
import fr.erias.edsabbs.graph.IStoreAllContextWords;
import fr.erias.edsabbs.metrics.ComputeMetrics;
import fr.erias.edsabbs.nounphrases.NounPhrasesTestAlgo2;

public class DetectShortFormsTest {

	public static IStoreAllContextWords getIContextafterShortDetection() {
		INounphrases nounphrases = new NounPhrasesTestAlgo2();
		DetectShortForms detectShortForms = new DetectShortForms(nounphrases);
		return(detectShortForms.getContextWords());
	}
	
	@Test
	public void DetectShortTest() {
		IStoreAllContextWords contextWords = DetectShortFormsTest.getIContextafterShortDetection();
		assertTrue(contextWords.isAcontextWord("sylvien"));
		assertTrue(contextWords.isAcontextWord("ischemique"));
		assertTrue(contextWords.getContexts().size() == 2); // AVC and IDM
		for (IOneContext context : contextWords.getContexts()) {
			assertTrue(context.getShortCandidates().size() == 1); // avc or idm
			for (ICandidate shortCandidate : context.getShortCandidates()) {
				assertTrue(shortCandidate.getLabel().equals("avc") || 
						shortCandidate.getLabel().equals("idm"));
			}
		}
	}

	@Test
	public void LongWordTest() {
		INounphrases nounphrases = new NounPhrasesTestAlgo2();
		IStoreAllContextWords contextWords = getIContextafterShortDetection();
		DetectLongForms detectLongForms = new DetectLongForms(nounphrases, contextWords);
		boolean found_avc = false;
		for (IOneContext context : detectLongForms.getContextWords().getContexts()) {
			for (ICandidate candidate : context.getLongCandidates()) {
				if (candidate.getLabel().equals("accident vasculaire cerebral")) {
					found_avc = true;
				}
			}
		}
		assertTrue(found_avc);
	}

	@Test
	public void MetricsTest() throws IOException {
		INounphrases nounphrases = new NounPhrasesTestAlgo2();
		IStoreAllContextWords contextWords = getIContextafterShortDetection();
		DetectLongForms detectLongForms = new DetectLongForms(nounphrases, contextWords);

		IStoreAllContextWords updatedContextWords = detectLongForms.getContextWords();
		ICandidatePairs candidatePairs = new CandidatePairs(updatedContextWords);

		candidatePairs.getShortForms();
		
		IWriteOutput writeOutput = new WriteOutputTest();
		ComputeMetrics computeMetrics = new ComputeMetrics(writeOutput);
		computeMetrics.compute(candidatePairs, nounphrases);
		
		String expectedString = "shortForm	freqShort	longForm	freqLong	intersectPDF	JaccardQuant	Dice	Cosine	CosineTFIDF	percentTFIDF	TFIDFminFreq	TFIDF\n" + 
				"idm	30	infarctus du myocarde	30	1.0	1.0	1.0	1.0	1.0	2.4	71.94	71.94\n" + 
				"avc	20	accident vasculaire cerebral	80	0.5	0.5	0.67	0.71	0.77	2.89	57.81	115.61";
		WriteOutputTest output = (WriteOutputTest) writeOutput;
		output.lines.equals(expectedString);
	}
}

