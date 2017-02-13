/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package edu.uit.snmr.view;
 
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import edu.uit.snmr.view.utils.GUIUtils;
 
public class ProgressBarDemo2 extends JDialog
                              implements PropertyChangeListener {
 
    private JProgressBar progressBar;
    private Task task;
 
    class Task extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            //Sleep for at least one second to simulate "startup".
            try {
                Thread.sleep(1000 + random.nextInt(2000));
            } catch (InterruptedException ignore) {}
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {}
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }
 
        /*
         * Executed in event dispatch thread
         */
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            dispose();
        }
    }
 
    public ProgressBarDemo2() {
        setLayout(new FlowLayout());
        
    	setLocationRelativeTo(null);
		setModal(true);		
		setResizable(false);
		setVisible(true);
		pack();
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        //progressBar.setIndeterminate(true);
        
        //Call setStringPainted now so that the progress bar height
        //stays the same whether or not the string is shown.
        
//        this.add(progressBar);
//        task = new Task();
//        task.addPropertyChangeListener(this);
//        task.execute();
    }
 
    
    /**
     * Invoked when task's progress property changes.
     */
    public void propertyChange(PropertyChangeEvent evt) {
    	System.out.println(evt.getPropertyName());
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setIndeterminate(false);
            progressBar.setValue(progress);
            
        }
    }
 
    
 
    public static void main(String[] args) {
    	 ProgressBarDemo2 newContentPane = new ProgressBarDemo2();
    }
}
