package fr.erias.abbsdetection.nounphrases;

/**
 * This class represents a term (noun phrase or unigram) and its frequency
 * 
 * @author cossins
 *
 */
public class TF extends SimpleTerm implements Comparable<TF>, ISimpleTerm{

	private int freq;
	
	public TF (String label, int freq) {
		super(label);
		this.freq = freq;
	}

	public int getFreq() {
		return freq;
	}
	
	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	@Override
	public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
  
        if (!(o instanceof TF)) {
            return false;
        }
        TF c = (TF) o;
		return(c.getWord().equals(this.label));
	}
	
	@Override
    public int hashCode() {
		return(this.label.hashCode());
	}

	@Override
	public int compareTo(TF o) {
		return(this.getWord().compareTo(o.getWord()));
	}
	
	@Override
	public String toString() {
		return ("TFword: " + this.getWord() + " with frequency " + this.getFreq());
	}
	
}
