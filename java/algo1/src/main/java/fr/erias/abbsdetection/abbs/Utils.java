package fr.erias.abbsdetection.abbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import fr.erias.IAMsystem.terminology.Term;
import fr.erias.IAMsystem.terminology.Terminology;
import fr.erias.IAMsystem.tokenizernormalizer.ITokenizerNormalizer;
import fr.erias.IAMsystem.tokenizernormalizer.TNoutput;
import fr.erias.edsabb.writer.IWriteOutput;

public class Utils {
	
	/**
	 * Reverse all the letter in a terminology
	 * @param terminology
	 */
	public static void reverseTerminology(Terminology terminology) {
		for (Term term : terminology.getTerms()) {
			String nl = term.getNormalizedLabel();
			String ln = new StringBuilder(nl).reverse().toString();
			term.setNormalizedLabel(ln);
		}
	}
	
	/**
	 * Create a terminology containing not containing unigrams
	 * @param in The inputstream of the CSV file
	 * @param sep the separator of the CSV file (ex : "\t")
	 * @param colLabel the ith column containing the libnormal (normalized label of the term)
	 * @param colCode the ith column containing the terminology code
	 * @param tokenizerNormalizer a {@link ITokenizerNormalizer} to tokenizer, normalize and ignore unigrams
	 * @throws IOException inputstream error
	 */
	public static Terminology getTerminologyRemoveUnigram(InputStream in, String sep, int colLabel, int colCode, ITokenizerNormalizer tokenizerNormalizer) throws IOException {
		Terminology terminology = new Terminology();
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		while ((line = br.readLine()) != null) {
			String[] columns = line.split(sep);
			String label = columns[colLabel];
			TNoutput tnoutput = tokenizerNormalizer.tokenizeNormalize(label);
			if (tnoutput.getTokens().length == 1) { // ignore unigram
				continue;
			}
			// add the normalize label
			String normalLabel = tnoutput.getNormalizedSentence();
			String code = columns[colCode];
			terminology.addTerm(normalLabel, code);
		}
		br.close();
		return(terminology);
	}
	
 
	/**
	 * write the terminology to a file
	 * @param outputFile the output CSV file
	 * @param sep file separator
	 * @throws IOException fail to write to file
	 */
	public static void writePrefixTokens2File(IWriteOutput writer, int n, HashSet<String> tokens, String sep) throws IOException {
		for (String term : tokens) {
			String prefix = term.substring(0,n);
			String newLine = prefix + sep + term + "\n";
			writer.write(newLine);
		}
	}
	
	/**
	 * write the terminology to a file
	 * @param outputFile the output CSV file
	 * @param sep file separator
	 * @throws IOException fail to write to file
	 */
	public static void writeSuffixTokens2File(IWriteOutput writer, int n, HashSet<String> tokens, String sep) throws IOException {
		for (String term : tokens) {
			String suffix = term.substring(0,n);
			term = new StringBuilder(term).reverse().toString();
			suffix = new StringBuilder(suffix).reverse().toString();
			String newLine = suffix + sep + term + "\n";
			writer.write(newLine);
		}
	}
}
