package fr.erias.abbsdetection.prefixDetector;

import java.io.IOException;
import java.util.Set;

import fr.erias.IAMsystem.ct.CTcode;
import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.edsabb.writer.IWriteOutput;

/**
 * An abbreviation detected within a CTcode (with IAMsystem)
 * 
 * @author Cossin Sebastien 
 */
public class WordAbbCT {

	private final WordAbb wordAbb; 

	private CTcode ct;

	public WordAbbCT(WordAbb wordAbb, CTcode ct) {
		this.ct = ct;
		this.wordAbb = wordAbb;
	}

	public CTcode getCTcode() {
		return(this.ct);
	}

	public WordAbb getWordAbb() {
		return(this.wordAbb);
	}

	@Override
	public String toString() {
		return(this.wordAbb.toString() + "\t" + this.ct.getCandidateTerm() + "\t" + this.ct.getTerm().getNormalizedLabel());
	}
	
	public static String getHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("shortForm");
		sb.append("\t");
		sb.append("longForm");
		sb.append("\t");
		sb.append("TermShortForm");
		sb.append("\t");
		sb.append("FreqTermShortForm");
		sb.append("\t");
		sb.append("TermLongForm");
		sb.append("\t");
		sb.append("FreqTermLongForm");
		return(sb.toString());
	}

	public String getStringOutput(INounphrases nounphrases) {
		int freqLabelTermino = nounphrases.getTF(this.ct.getTerm().getLabel()).getFreq();
		int freqLabelText = nounphrases.getTF(this.ct.getCandidateTerm()).getFreq();
		String outputString = this.wordAbb.toString() + "\t" + 
				this.ct.getCandidateTerm() +  "\t" + freqLabelText + "\t" +
				this.ct.getTerm().getNormalizedLabel() +  "\t" + freqLabelTermino; 
		return(outputString);
	}

	/******************************* Getters ******************************/

	/**
	 * Output the result
	 * @param outputFile Write the result to an output file
	 * @param wordAbbs Set of Words Abbreviations
	 * @throws IOException can't write to the file
	 */
	public static void writeWordAbbs(IWriteOutput writer, Set<? extends WordAbbCT> wordAbbs) throws IOException {
		for (WordAbbCT wordAbb : wordAbbs) {
			String newLine = wordAbb.toString() + "\n";
			writer.write(newLine);
		}
	}
}
