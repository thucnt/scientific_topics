package edu.uit.snmr.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NumberOnlyKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent ke) {
		char c = ke.getKeyChar();
		if ((!(Character.isDigit(c))) && // Only digits
				(c != '\b')) // For backspace
		{
			ke.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}

class DoubleOnlyKeyListener implements KeyListener {

	@Override
	public void keyTyped(KeyEvent ke) {
		char c = ke.getKeyChar();
		if ((!(Character.isDigit(c))) && // Only digits
				(c != '\b')&&(c!='.')) // For backspace
		{
			ke.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
