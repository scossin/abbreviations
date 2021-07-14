package fr.erias.abbsdetection.prefixDetector;

/**
 * A class representing an abbreviation
 * 
 * @author Cossin Sebastien 
 *
 */
public class WordAbb {

	/**
	 * The short form of an abbreviation
	 * ex: "insuf"
	 */
	protected final String shortForm;


	/**
	 * The long form of an abbreviation
	 * ex: "insuffisance"
	 */
	protected final String longForm;

	/**
	 * Create a new abbreviation
	 * @param shortForm The short form of an abbreviation
	 * @param longForm The long form of an abbreviation
	 */
	public WordAbb(String shortForm, String longForm) {
		this.shortForm = shortForm;
		this.longForm = longForm;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof WordAbb)) {
			return(false);
		}
		WordAbb wordAbb = (WordAbb) o;
		return (this.shortForm.equals(wordAbb.getShortForm()) && 
				this.longForm.equals(wordAbb.getLongForm()));
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + this.shortForm.hashCode();
		result = 31 * result + this.longForm.hashCode();
		return(result);
	}

	@Override
	public String toString() {
		return(this.shortForm + "\t" + this.longForm);
	}

	/******************************* Getters ******************************/

	public String getShortForm() {
		return shortForm;
	}

	public String getLongForm() {
		return longForm;
	}
}
