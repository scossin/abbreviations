package fr.erias.abbsdetection.stopwords;

import java.io.IOException;
import java.io.InputStream;

import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.IAMsystem.stopwords.StopwordsImpl;

public class StopwordsAbbreviation {
	
	private static final String stopwordsFilename = "stopwords.txt";
	
	public static IStopwords getStopwords() throws IOException {
		StopwordsImpl stopwords = new StopwordsImpl();
		InputStream inStopwords  = Thread.currentThread().getContextClassLoader().getResourceAsStream(stopwordsFilename);
		stopwords.setStopWords(inStopwords);
		inStopwords.close();
		return(stopwords);
	}
	
}
