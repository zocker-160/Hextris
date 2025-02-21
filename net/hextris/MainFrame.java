package net.hextris;

import javax.swing.*;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;

/**
 * the hextris window
 * 
 * @author fränk
 *
 */
public class MainFrame extends JFrame 
{
	private static final long serialVersionUID = -2432811889021456787L;

	private Hextris hextris = null;
	private Context ctx = Context.getContext();
	
	public MainFrame(Hextris h) 
	{
		super("Hextris " + Hextris.getVersion());
		this.hextris = h;
		initialize();
		
	}

	/**
	 * create widgets and stuff
	 *
	 */
	public void initialize() 
	{
//		try {
//		UIManager.setLookAndFeel(
//			UIManager.getSystemLookAndFeelClassName());
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
		this.setContentPane(this.hextris);
		//this.setSize(hextris.getWidth(), hextris.getHeight());
		this.setResizable(false);
		this.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		//menubar
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('g');
		JMenu helpMenu = new JMenu("?");
		JMenuItem newGameMI = new JMenuItem("New Game");
		JMenuItem newDemoMI = new JMenuItem("Demo");
		JMenuItem highScoresMI = new JMenuItem("Highscores");
		JMenuItem prefsMI = new JMenuItem("Preferences");
		JMenuItem exitMI = new JMenuItem("Quit");
		JMenuItem gameInfo = new JMenuItem("Info");
		JMenuItem gameHelp = new JMenuItem("Help");
		gameMenu.add(newGameMI);
		gameMenu.add(newDemoMI);
		gameMenu.add(highScoresMI);
		gameMenu.add(prefsMI);
		gameMenu.add(exitMI);
		helpMenu.add(gameInfo);
		helpMenu.add(gameHelp);
		menuBar.add(gameMenu);
		menuBar.add(helpMenu);
		newGameMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.Event.ALT_MASK, false));
		newGameMI.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				hextris.newGame(false);
			}
		});
		newDemoMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.Event.ALT_MASK, false));
		newDemoMI.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				hextris.newGame(true);
			}
		});
		highScoresMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.Event.ALT_MASK, false));
		highScoresMI.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				hextris.showHighScores();
			}
		});
		prefsMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.Event.ALT_MASK, false));
		prefsMI.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				showGameProperties();
			}
		});
		exitMI.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.Event.ALT_MASK, false));
		exitMI.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				exit();
			}
		});
		gameInfo.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				showGameInfo();
			}
		});
		gameHelp.addActionListener(new java.awt.event.ActionListener() { 
			public void actionPerformed(java.awt.event.ActionEvent e) {    
				showGameHelp();
			}
		});

		this.hextris.addKeyListener(new java.awt.event.KeyAdapter() { 
			public void keyPressed(java.awt.event.KeyEvent e) {
				hextris.gameKeyPressed(e);
			}
		});
		

		this.setJMenuBar(menuBar);
		this.hextris.setFocusable(true);
		this.hextris.setRequestFocusEnabled(true);
		//this.hextris.highScores.read();
		this.hextris.setBackground(java.awt.Color.lightGray);

		this.setIconImage(Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/net/hextris/hextris_icon.gif")));		
		
		this.pack();
		this.setLocationRelativeTo(null);
		//this.setLocationByPlatform(true);
		this.setVisible(true);
	}
	
	public void dispose() 
	{
		super.dispose();
		hextris.setMoverThread(null);
	}
	
	/**
	 * bring up the properies dialog 
	 *
	 */
	private void showGameProperties()
	{

		PrefsDlg dlg = new PrefsDlg(JOptionPane.getFrameForComponent(this));
		dlg.setLocationRelativeTo(this);
		dlg.setVisible(true);
		this.pack();
	}

	/**
	 * show a little info dialog
	 *
	 */
	private void showGameInfo()
	{
		JTextArea jta = new JTextArea("Tetris with Hexagons\n\n" +
				"written by Frank Felfe\n\n" +
				"based on an idea by David Markley\n" + 
				"and code from Java-Tetris by Christian Schneider\n\n" +
				"released under the terms of the GNU General Public License");
		jta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), 
				jta,
				"Hextris " + Hextris.getVersion(),
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * show a little help window 
	 *
	 */
	private void showGameHelp()
	{
		JTextArea jta = new JTextArea(
				"Please visit the hextris homepage at\n" + 
				"http://hextris.inner-space.de/\n\n" + 
				"Keys:\n" +
				"move left - " + KeyEvent.getKeyText(ctx.getKeys()[0]) + " or Left\n" +
				"move right - " + KeyEvent.getKeyText(ctx.getKeys()[1]) + " or Right\n" +
				"rotate clockwise - " + KeyEvent.getKeyText(ctx.getKeys()[3]) + " or Up\n" +
				"rotate counterclockwise - " + KeyEvent.getKeyText(ctx.getKeys()[2]) + "\n" + 
				"move down - " + KeyEvent.getKeyText(ctx.getKeys()[4]) + " or Down\n" +
				"fall down - " + KeyEvent.getKeyText(ctx.getKeys()[5]) + " or Space\n\n" + 
				"Score:\n" + 
				"move down - level x severity\n" + 
				"fall down - level x severity x 2 x lines\n" + 
				"stone - 10 x level x severity\n" + 
				"line removed - 100 x level x severity x lines");
		jta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), 
				jta,
				"Hextris Help",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void exit()
	{
		System.exit(0);
	}
	
	protected void processWindowEvent (WindowEvent e) {
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	        exit();
	      }
	}

	public void setHextris(Hextris h)
	{
		this.hextris = h;
	}
	
} 
