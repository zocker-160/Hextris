package net.hextris;

import javax.swing.JApplet;
import java.awt.event.KeyEvent;

/**
 * Starting point for use of hextris as an applet.
 * This class simply loads the Hextris panel class
 * as it´s content pane
 * 
 * @author frank
 *
 */
public class HextrisApplet extends JApplet {

	private static final long serialVersionUID = -8291800152310127713L;
	private Hextris hextris = null;
	
	public HextrisApplet() {
		super();		
	}

	public void init() {
		Hextris hextris = getHextris();
		this.setContentPane(hextris);
		this.setSize(320, 300);
		this.setLocation(0, 0);
		
		hextris.addKeyListener(new java.awt.event.KeyAdapter() { 
			public void keyPressed(java.awt.event.KeyEvent e) {
				gameKeyPressed(e);
			}
		});

	}
	
	public void start() {
			super.start();
		}
	
	public void stop() {
		super.stop();
		hextris.gameOver();
	}

	private Hextris getHextris() {
		if(hextris == null) {
			hextris = new Hextris(true);
			hextris.setFocusable(true);
			hextris.setRequestFocusEnabled(true);
		}
		return hextris;
	}
	
	private void gameKeyPressed(KeyEvent e) 
	{
		int kc = e.getKeyCode();
		int km = e.getModifiers();
		
		if (kc == KeyEvent.VK_N && (km&KeyEvent.ALT_MASK)!=0) {
			this.getHextris().newGame(false);
		} else if (kc == KeyEvent.VK_D && (km&KeyEvent.ALT_MASK)!=0) {
			this.getHextris().newGame(true);
		} else {
			this.getHextris().gameKeyPressed(e);
		}
	}		
}
