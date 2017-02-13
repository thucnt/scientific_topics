/**
 * 
 */
package edu.uit.snmr.utils;



/**
 * @author muonnv
 *
 */
public class RandomGenerator {
	
	/* A couple of #define specifically for rng */
	public static final double  RANDOMMAX = 4294967296.0; //2147483647			// = 2^32
		
	private MersenneTwister mtRandom;
	private static RandomGenerator instance;
	
	private RandomGenerator () {
		this.mtRandom = new MersenneTwister();
	}
	
	public static RandomGenerator getInstance() {
		if (instance == null) 
		{
			instance = new RandomGenerator();
		}
		return instance;
	}
	
		
	public synchronized double randomNumber(int seed) {
		
		return mtRandom.nextInt(seed) / RANDOMMAX;
	}
	
	public synchronized double randomNumber() {
		int rn = mtRandom.next(32);
		// first, convert the int from signed to "unsigned"
		long unsignedNumber = rn  & 0x00000000ffffffffL;
		return unsignedNumber / RANDOMMAX;
	}
	
	public synchronized void setSeed(int seed) {
		mtRandom.setSeed(seed); // set seed for random number generator
	}
	
	public static void main(String[] args) {
		System.out.println(Math.pow(2, 32));
		RandomGenerator instance = RandomGenerator.getInstance();
		instance.setSeed(1);		
		System.out.println(instance.randomNumber());
	}
}
