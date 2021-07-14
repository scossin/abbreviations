package fr.erias.edsabbs.shortform;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;

import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.edsabbs.detection.DetectShortForms;
import fr.erias.edsabbs.nounphrases.NounPhrasesTestAlgo2;

public class ShortFormTest {

//	@Test
//	public void contextWordTest() throws IOException {
//		INounphrases nounphrases = new NounPhrasesTestAlgo2();
//		DetectShortForms detectShort = new DetectShortForms(nounphrases);
//		String[] contextTokens = {"du"}; // no context word, there is only a stopword
//		String contextword = detectShort.getContextWord(contextTokens);
//		assertNull(contextword);
//		
//		String[] contextTokensMyocarde = {"du", "myocarde"}; // myocarde is a context word
//		contextword = detectShort.getContextWord(contextTokensMyocarde);
//		assertEquals(contextword, "myocarde");
//	}
}
