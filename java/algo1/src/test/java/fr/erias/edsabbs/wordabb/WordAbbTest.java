package fr.erias.edsabbs.wordabb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import fr.erias.abbsdetection.prefixDetector.WordAbb;

public class WordAbbTest {
	
	@Test
	public void IsStopwordTest() {
		HashSet<WordAbb> wordAbbs = new HashSet<WordAbb>();
		WordAbb wordAbb = new WordAbb("avc","accident vasculaire cerebral");
		wordAbbs.add(wordAbb);


		WordAbb wordAbb2 = new WordAbb("avc","accident vasculaire cerebral");
		wordAbbs.add(wordAbb2);

		assertEquals(wordAbbs.size(), 1);
	}
}
