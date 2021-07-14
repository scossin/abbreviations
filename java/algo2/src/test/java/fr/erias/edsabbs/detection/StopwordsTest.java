package fr.erias.edsabbs.detection;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import fr.erias.edsabbs.stopwords.StopwordsAlgo2;

public class StopwordsTest {

	@Test
	public void IsStopwordTest() throws IOException {
		assertTrue(StopwordsAlgo2.INSTANCE.isStopWord("de"));
	}
}
