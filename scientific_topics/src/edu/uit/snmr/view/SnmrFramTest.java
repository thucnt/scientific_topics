package edu.uit.snmr.view;
import java.awt.Dimension;
/**
 * 
 */

/**
 * @author muonnv
 *
 */
public class SnmrFramTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SnmrFrame frame = new SnmrFrame(new Dimension(800, 600));
				frame.setTitle("Social Network Marketing Recommendation System");		
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});

	}
}
