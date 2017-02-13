/**
 * 
 */
package edu.uit.snmr.dataset.termextractor.technical;

/**
 * @author muonnv
 *
 */
public class ProbEntry {

	private String name;
	
	private double probability;

	
	/**
	 * @param name
	 * @param probability
	 */
	public ProbEntry(String name, double probability) {
		this.name = name;
		this.probability = probability;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}

	/**
	 * @param probability the probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	

}
