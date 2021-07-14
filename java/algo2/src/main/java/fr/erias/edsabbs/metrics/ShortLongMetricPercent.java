package fr.erias.edsabbs.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.erias.IAMsystem.tokenizer.ITokenizer;
import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.abbsdetection.nounphrases.TF;
import fr.erias.abbsdetection.tokenizer.AbbTokenizer;
import fr.erias.edsabbs.graph.ICandidate;
import fr.erias.edsabbs.graph.ICommonContext;
import fr.erias.edsabbs.graph.IShortLongCandidate;

public class ShortLongMetricPercent implements Comparable<ShortLongMetricPercent> {
	
	private final static Logger logger = LoggerFactory.getLogger(ShortLongMetricPercent.class);
	private ITokenizer tokenizer = AbbTokenizer.tokenizer;
	private double percent = 0;
	private IShortLongCandidate shortLongCandidate;
	private INounphrases nounphrases;
	
	public ShortLongMetricPercent (IShortLongCandidate shortLongCandidate, INounphrases nounphrases) {
		this.nounphrases = nounphrases;
		this.shortLongCandidate = shortLongCandidate;
		setMetric();
	}
	
	public IShortLongCandidate getShortLongCandidate() {
		return(this.shortLongCandidate);
	}
	
	public static String getHeader() {
		String[] headers = {"shortForm","freqShort","longForm","freqLong",
				"intersectPDF","JaccardQuant","Dice","Cosine","CosineTFIDF",
				"percentTFIDF","TFIDFminFreq","TFIDF"};
		StringBuilder sb = new StringBuilder();
		for (String header : headers) {
			sb.append(header);
			sb.append("\t");
		}
		sb.setLength(sb.length() - 1);// remove the last \t
		return(sb.toString());
	}
	
	private double round(double valueToRound) {
		double value = valueToRound*100;
		value = Math.round(value);
		value = value /100;
		return(value);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String sep = "\t";
		sb.append(shortLongCandidate.getShortCandidate().getLabel());
		sb.append(sep);
		sb.append(shortLongCandidate.getShortCandidate().getFreq());
		sb.append(sep);
		sb.append(shortLongCandidate.getLongCandidate().getLabel());
		sb.append(sep);
		sb.append(shortLongCandidate.getLongCandidate().getFreq());
		sb.append(sep);
		// metrics:
		sb.append(round(this.getIntersectPDF()));
		sb.append(sep);
		
		sb.append(round(this.JaccardQuant()));
		sb.append(sep);
		
		sb.append(round(this.getDice()));
		sb.append(sep);
		
		sb.append(round(this.getCosine()));
		sb.append(sep);
		
		sb.append(round(this.getCosineTFIDF()));
		sb.append(sep);
		
		sb.append(round(this.getPercentTFIDF()));
		sb.append(sep);
		
		
		sb.append(round(this.getTFIDFminFreq()));
		sb.append(sep);
		
		sb.append(round(this.getTFIDF()));
		return(sb.toString());
	}
	
	/**
	 * Simply the sum of Pi when Qi is not 0 
	 * Pi: probability of word i in longForm (ex: P("sylvien"|"accident vasculaire cerebral")
	 */
	@Deprecated
	private double getPercentLongForm() {
		double percent = 0 ;
		double totalLongForm = shortLongCandidate.getLongCandidate().getFreq() * 1.0;
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			percent = percent + commonContext.getTermLongForm().getFreq() * 1.0 / totalLongForm;
		}
		return(percent);
	}
	
	/**
	 * Equation 12. Sung-Hyuk Cha. Comprehensive Survey on Distance / Similarity measures
	 */
	private double getIntersectPDF() {
		double metric = 0 ;
		long totalLongForm = this.shortLongCandidate.getLongCandidate().getFreq();
		long totalShortForm = this.shortLongCandidate.getShortCandidate().getFreq();
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			double pi = commonContext.getTermLongForm().getFreq() * 1.0 / totalLongForm * 1.0 ;
			double qi = commonContext.getTermShortForm().getFreq() * 1.0 / totalShortForm * 1.0 ;
			double minimum = Math.min(pi, qi); // since we take the minimum, lemmaTerms not in common = 0;
			metric = metric + minimum;
		}
		return(metric);
	}
	
	/**
	 * Equation 22. Sung-Hyuk Cha. Comprehensive Survey on Distance / Similarity measures
	 * @return
	 */
	private double JaccardQuant() {
		long totalLongForm = this.shortLongCandidate.getLongCandidate().getFreq();
		long totalShortForm = this.shortLongCandidate.getShortCandidate().getFreq();
		double sum_piqi = 0;
		//HashSet<LemmaTerm> terms = new HashSet<LemmaTerm>();
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			double pi = commonContext.getTermLongForm().getFreq() * 1.0 / totalLongForm * 1.0 ;
			double qi = commonContext.getTermShortForm().getFreq() * 1.0 / totalShortForm * 1.0 ;
			sum_piqi = sum_piqi + pi * qi;
		}
		double sum_pi_square = 0;
		for (TF lemmaTerm : this.shortLongCandidate.getLongCandidate().getTFs()) {
			double pi = lemmaTerm.getFreq() * 1.0 / totalLongForm * 1.0;
			sum_pi_square = sum_pi_square + pi * pi;
		}
		
		double sum_qi_square = 0;
		for (TF lemmaTerm : this.shortLongCandidate.getShortCandidate().getTFs()) {
			double qi = lemmaTerm.getFreq() * 1.0 / totalShortForm * 1.0;
			sum_qi_square = sum_qi_square + qi * qi;
		}
		double jaccard = sum_piqi / (sum_pi_square  + sum_qi_square - sum_piqi);
		
		// to compute dice:
		this.sum_piqi = sum_piqi;
		this.sum_pi_square = sum_pi_square;
		this.sum_qi_square = sum_qi_square;
		return(jaccard);
	}
	private double sum_piqi = 0;
	private double sum_pi_square = 0;
	private double sum_qi_square = 0;
	
	/**
	 * Equation 23. Sung-Hyuk Cha. Comprehensive Survey on Distance / Similarity measures
	 * @return
	 */
	private double getDice() {
		double metric = 2 * sum_piqi / (sum_pi_square + sum_qi_square);
		return(metric);
	}
	
	private double jaccardClass() {
		int N1 = this.getShortLongCandidate().getLongCandidate().getTFs().size();
		int N2 = this.getShortLongCandidate().getShortCandidate().getTFs().size();
		int common =  shortLongCandidate.getCommonContexts().size();
		double jaccard = common * 1.0 / (N1+N2-common) * 1.0;
		return(jaccard);
	}
	
	/**
	 * Equation 20. Sung-Hyuk Cha. Comprehensive Survey on Distance / Similarity measures 
	 * Cosine using the TF
	 * @return
	 */
	private double getCosine() {
		double metric = 0;
		if (this.shortLongCandidate.getLongCandidate().getNormValue() == 0) {
			return 0;
		}
		int numerator = 0;
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			numerator = numerator + commonContext.getTermLongForm().getFreq() * commonContext.getTermShortForm().getFreq();
		}
		double denominator = this.shortLongCandidate.getLongCandidate().getNormValue() * this.shortLongCandidate.getShortCandidate().getNormValue();
		metric = numerator * 1.0 / denominator;
		return(metric);
	}
	
	/**
	 * Equation 20. Sung-Hyuk Cha. Comprehensive Survey on Distance / Similarity measures 
	 * Cosine using the TF*IDF 
	 * @return
	 */
	private double getCosineTFIDF() {
		double metric = 0;
		if (this.shortLongCandidate.getLongCandidate().getNormValueTFIDF() == 0) {
			return 0;
		}
		double numerator = 0;
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			String contextWord = commonContext.getContextWord();
			double idf = nounphrases.getIDF(contextWord);
			numerator = numerator + commonContext.getTermLongForm().getFreq() * idf * commonContext.getTermShortForm().getFreq() * idf;
		}
		logger.debug("numerator: " + numerator);
		double denominator = this.shortLongCandidate.getLongCandidate().getNormValueTFIDF() * this.shortLongCandidate.getShortCandidate().getNormValueTFIDF();
		metric = numerator / denominator;
		logger.debug("denominator: " + denominator);
		return(metric);
	}
	
	/**
	 * getTF-IDF common (the one I invented :-)
	 * @return
	 */
	private double getTFIDF() {
		double metric = 0;
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			int freqLong = commonContext.getTermLongForm().getFreq();
			int freqShort = commonContext.getTermShortForm().getFreq();
			int maxFreq = Math.max(freqLong, freqShort);
			String contextWord = commonContext.getContextWord();
			double idf = nounphrases.getIDF(contextWord);
			metric = metric +  maxFreq * idf; 
		}
		return(metric);
	}
	
	
	/**
	 * getTF-IDF common (the one I invented :-)
	 * @return
	 */
	private double getTFIDFminFreq() {
		double metric = 0;
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			int freqLong = commonContext.getTermLongForm().getFreq();
			int freqShort = commonContext.getTermShortForm().getFreq();
			int minFreq = Math.min(freqLong, freqShort);
			String contextWord = commonContext.getContextWord();
			double idf = nounphrases.getIDF(contextWord);
			metric = metric +  minFreq * idf; 
		}
		return(metric);
	}
	
	/**
	 * Same af TF-IDF but replace the TF par the percent of TF
	 * @return
	 */
	private double getPercentTFIDF() {
		long totalLongForm = this.shortLongCandidate.getLongCandidate().getFreq();
		long totalShortForm = this.shortLongCandidate.getShortCandidate().getFreq();
		double metric = 0;
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			double percentLong = commonContext.getTermLongForm().getFreq() * 1.0 / totalLongForm * 1.0;
			double percentShort = commonContext.getTermShortForm().getFreq() * 1.0 / totalShortForm * 1.0 ;
			double maxPercent = Math.max(percentLong, percentShort);
			String contextWord = commonContext.getContextWord();
			double idf = nounphrases.getIDF(contextWord);
			metric = metric +  maxPercent * idf; 
		}
		return(metric);
	}
	
	/**
	 * getTF-IDF common (the one I invented :-)
	 * @return
	 */
	private double getTFIDFmin() {
		double metric = 0;
		for (ICommonContext commonContext : shortLongCandidate.getCommonContexts()) {
			int freqLong = commonContext.getTermLongForm().getFreq();
			int freqShort = commonContext.getTermShortForm().getFreq();
			int maxFreq = freqLong + freqShort;
			String contextWord = commonContext.getContextWord();
			double idf = nounphrases.getIDF(contextWord);
			metric = metric +  maxFreq * idf; 
		}
		return(metric);
	}
	
	/**
	 * The metric used to sort
	 */
	private void setMetric() {
		ICandidate longCandidate = this.shortLongCandidate.getLongCandidate();
		int nTokensLong = tokenizer.tokenize(longCandidate.getLabel()).length;
		this.percent = this.getCosine() * nTokensLong; // cosine similarity * number of tokens of long forms
	}
	
	public double getPercent() {
		return(this.percent);
	}

	@Override
	public int compareTo(ShortLongMetricPercent o) {
		double diff = o.getPercent() - this.percent;
		if (diff == 0) {
			return -1;
		}
		if (diff < 0) {
			return -1;
		}
		if (diff > 0) {
			return 1;
		}
		return 1; // unreachable
	}
}
