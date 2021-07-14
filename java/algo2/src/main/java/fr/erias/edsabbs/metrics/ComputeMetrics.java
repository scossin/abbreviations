package fr.erias.edsabbs.metrics;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import fr.erias.abbsdetection.nounphrases.INounphrases;
import fr.erias.edsabb.writer.IWriteOutput;
import fr.erias.edsabbs.graph.ICandidatePairs;
import fr.erias.edsabbs.graph.IShortLongCandidate;

/**
 * This class is used to compute metrics between 2 pairs (short and long candidates) and write the results
 * @author Sebastien Cossin
 *
 */
public class ComputeMetrics {
	
	private IWriteOutput writeOutput;
	
	private final int nResultsByShortForm = 20;

	public ComputeMetrics(IWriteOutput writeOutput) throws IOException {
		this.writeOutput = writeOutput;
		writeOutput.write(ShortLongMetricPercent.getHeader() + "\n");
	}
	
	public void compute(ICandidatePairs candidatePairs, INounphrases nounphrases) throws IOException {
		for (String shortForm : candidatePairs.getShortForms()) {
			TreeSet<ShortLongMetricPercent> tree = new TreeSet<ShortLongMetricPercent>(); // to order by metric
			for (IShortLongCandidate shortLongCandidate : candidatePairs.getShortLongCandidates(shortForm)) {
				ShortLongMetricPercent shortLongMetricPercent = new ShortLongMetricPercent(shortLongCandidate, nounphrases);
				tree.add(shortLongMetricPercent);
			}
			int count = 0;
			Iterator<ShortLongMetricPercent> iterMetric = tree.iterator();
			while(iterMetric.hasNext()) { // select the 20 first
				ShortLongMetricPercent shortLongMetricPercent = iterMetric.next();
				String line = shortLongMetricPercent.toString() + "\n";
				writeOutput.write(line);
				count = count + 1;
				if (this.nResultsByShortForm == count) {
					break;
				}
			}
		}
	}
}
