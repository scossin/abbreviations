package fr.erias.edsabbs.stopwords;

import java.io.IOException;

import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.abbsdetection.stopwords.StopwordsAbbreviation;

public class StopwordsAlgo2 implements IStopwords {

	public static IStopwords INSTANCE = new StopwordsAlgo2();
	
	private IStopwords stopwords;
	
	private StopwordsAlgo2() {
		try {
			this.stopwords = StopwordsAbbreviation.getStopwords();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isStopWord(String token) {
		// TODO Auto-generated method stub
		return stopwords.isStopWord(token);
	}

	@Override
	public void addStopwords(String token) {
		stopwords.addStopwords(token);
	}
}
