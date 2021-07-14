package fr.erias.abbsdetection.prefixDetector;

import java.io.IOException;
import java.util.HashSet;

import fr.erias.IAMsystem.detect.DetectDictionaryEntry;
import fr.erias.IAMsystem.detect.DetectOutput;
import fr.erias.IAMsystem.detect.ITermDetector;
import fr.erias.IAMsystem.normalizer.Normalizer;
import fr.erias.IAMsystem.stopwords.IStopwords;
import fr.erias.IAMsystem.stopwords.StopwordsImpl;
import fr.erias.IAMsystem.synonym.ISynonym;
import fr.erias.IAMsystem.synonym.PrefixDetector;
import fr.erias.IAMsystem.terminology.Terminology;
import fr.erias.IAMsystem.tokenizernormalizer.ITokenizerNormalizer;
import fr.erias.IAMsystem.tokenizernormalizer.TokenizerNormalizer;
import fr.erias.IAMsystem.tree.SetTokenTree;
import fr.erias.IAMsystem.tree.SetTokenTreeBuilder;
import fr.erias.IAMsystem.utils.Utils;
import fr.erias.abbsdetection.stopwords.StopwordsAbbreviation;

public class TroncationDetector implements ITermDetector {

	private DetectDictionaryEntry detectDictionaryEntry;
	
	private ITokenizerNormalizer tokenizerNormalizer;
	
	public TroncationDetector(Terminology terminology) throws IOException {
		this.createDetectDicEntry(terminology);
	}
	
	private void createDetectDicEntry(Terminology terminology) throws IOException {
		HashSet<ISynonym> synonyms = new HashSet<ISynonym>();
		SetTokenTree setTokenTree = new SetTokenTree();
		tokenizerNormalizer = TokenizerNormalizer.getDefaultTokenizerNormalizer();
		IStopwords stopwords = StopwordsAbbreviation.getStopwords();
		Normalizer normalizerTerm = new Normalizer(stopwords);
		tokenizerNormalizer.setNormalizer(normalizerTerm);
		
		
		// Empty stopwords for detection
		stopwords = new StopwordsImpl();
		tokenizerNormalizer.getNormalizer().setStopwords(stopwords);
		SetTokenTreeBuilder.loadTokenTree(terminology, setTokenTree, tokenizerNormalizer);
		
		// get uniqueTokens from the terminology for prefix detection
		HashSet<String> uniqueTokensTermino = Utils.getUniqueToken(terminology, tokenizerNormalizer);
		// create prefixes for each token
		int minPrefixLength = 3;
		ISynonym abbDetection = new PrefixDetector(uniqueTokensTermino, minPrefixLength);
		synonyms.add(abbDetection);
		this.detectDictionaryEntry = new DetectDictionaryEntry(setTokenTree,tokenizerNormalizer,synonyms);
	}

	@Override
	public DetectOutput detect(String sentence) {
		return (this.detectDictionaryEntry.detectCandidateTerm(sentence));
	}
	
	public ITokenizerNormalizer getTokenizerNormalizer() {
		return tokenizerNormalizer;
	}
}
