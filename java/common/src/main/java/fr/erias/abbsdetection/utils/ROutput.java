package fr.erias.abbsdetection.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import fr.erias.abbsdetection.nounphrases.TF;

/**
 * A R program extract all candidate terms (with CandidateTerm API), the output 
 * consists of multiple tabulated files in FOLDER_R
 * This class reads all files and put the results in a single file
 * @author Sebastien Cossin
 *
 */
public class ROutput {
	
	/**
	 * Count each lemmaTerm
	 */
	private HashMap<String, Integer> mapLemmaFreq = new HashMap<String, Integer>(); 
	
	public ROutput(File folder, File outputFile) throws IOException {
		if (!folder.isDirectory()) {
			throw new FileNotFoundException(folder.getAbsolutePath() + " is not a folder");
		}
		File[] files = folder.listFiles();
		for (File file : files) {
			readFile(file);
		}
		writeFile(outputFile);
	}
	
	private void readFile(File tabFile) throws IOException {
		InputStream in = new FileInputStream(tabFile);
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		br.readLine(); // ignore header
		while ((line = br.readLine()) != null) {
			String[] columns = line.split("\t");
			if (columns.length != 2) {
				continue;
			}
			String label = columns[0];
			label = label.trim();
			String freq = columns[1];
			int count = Integer.parseInt(freq);
			if (!this.mapLemmaFreq.containsKey(label)) {
				this.mapLemmaFreq.put(label, 0);
			}
			int previousCount = this.mapLemmaFreq.get(label);
			this.mapLemmaFreq.put(label, count + previousCount);
		}
		br.close();
		in.close();
	}
	
	private void writeFile(File outputFile) throws IOException {
		System.out.println("writing results");
		System.out.println(this.mapLemmaFreq.size() + " lemmaTerms");
		// order each lemmaTerm by its label (alphabetical order)
		TreeSet<TF> tfs = new TreeSet<TF>(); 
		outputFile.delete();
		outputFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(outputFile, true);
		Iterator<String> iter = this.mapLemmaFreq.keySet().iterator();
		while (iter.hasNext()) {
			String label = iter.next();
			int count = this.mapLemmaFreq.get(label);
			if (count == 1) {
				continue;
			}
			TF tf = new TF(label, count);
			tfs.add(tf);
		}
		for (TF tf : tfs) {
			String newLine = tf.getWord() + "\t" + tf.getFreq() + "\n";
			fos.write(newLine.getBytes());
		}
		fos.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		String folderOfTabulatedFiles = ""; // the folder containing multiples tabulated files
		File folder = new File(folderOfTabulatedFiles);
		String singleTabFile = ""; // the filename concatenating all files
		File outputFile = new File(singleTabFile);
		new ROutput(folder, outputFile);
	}
}
