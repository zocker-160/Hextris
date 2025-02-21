package net.hextris;

import java.awt.event.KeyEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Hextris main class
 * 
 * Implements the Controller part of the Hextris game.
 * Reacts on keyboard input and starts a thread for
 * moving the stone down after a certain amount of time.
 *
 * @author fränk
 */
public class Hextris extends JPanel implements Runnable 
{
	private static final long serialVersionUID = -3267887732569843668L;
	private static final String version = "0.9.7";
	
	private static final int NONE = 0;
	private static final int MOVE_DOWN = 1;
	private static final int FALL_DOWN = 2;
	
	private int action = NONE;

	private Context ctx = Context.getContext();
	
	private GamePanel playPanel = null;
	private GamePanel previewPanel = null;
	private JLabel scoreLabel = new JLabel();
	private JLabel levelLabel = new JLabel();
	private JLabel stonesLabel = new JLabel();
	private JLabel linesLabel = new JLabel();	
	private Object[] startMsg = null;
	private JComboBox severityCB = null;
	private JComboBox levelCB = null;
	private JButton buttonStart=null;
	private JButton buttonDemo=null;
	private JButton buttonHighscore=null;
	
	private Thread moverThread;
	private Stone currentStone;
	private Stone nextStone;
	private int lines;
	private int stones;
	private int score;
	private int level;
	private int severity;
	private boolean gameOver;
	private boolean demo = false;
	
	public Hextris(boolean buttons) 
	{
		super();
		initialize(buttons);
	}
	
	/**
	 * moverThread to move the stone down after a certain amount of time
	 * handles move down and fall down requests
	 */
	public void run() 
	{
		while (moverThread==Thread.currentThread()) {
			try {
				Thread.sleep(1800/(level+1)-100);
			} catch (InterruptedException e1) {}
			
			if (gameOver || this.currentStone==null) continue;

			switch (this.action) {
			case FALL_DOWN:
				this.fallDown();
				break;
			case MOVE_DOWN:
				this.addScore((severity+1) * getLevel());
			case NONE:
				moveDown();
				break;
			default: 
				System.out.println("no action: " + this.action);
				break;
			}
			this.action = NONE;
		}

	}
	
	/**
	 * widgets and stuff
	 * 
	 * @return void
	 */
	private void initialize(boolean buttons) 
	{
		this.setLayout(new GridBagLayout());
		this.setBackground(java.awt.Color.LIGHT_GRAY);
		if (playPanel==null) playPanel = new GamePanel(15,23);
		playPanel.setLocation(10, 10);
		this.add(playPanel,
                new GridBagConstraints(0, 0, 1, 10, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.BOTH,
                        new Insets(10, 10, 10, 10),
                        0, 0));
		
		if(previewPanel == null) previewPanel = new GamePanel(6,6);
		previewPanel.setLocation(20 + playPanel.pixelWidth, 10);
		previewPanel.setBackground(java.awt.Color.lightGray);
		this.add(new JLabel("Next:"),
                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(10, 0, 0, 10),
                        0, 0));
		this.add(previewPanel,
                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(0, 0, 0, 10),
                        0, 0));

		levelLabel.setPreferredSize(new Dimension(100, 19));
		levelLabel.setText("");
		levelLabel.setForeground(java.awt.Color.black);
		this.add(levelLabel,
                new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(10, 0, 0, 10),
                        0, 0));
		
		stonesLabel.setPreferredSize(new Dimension(100, 19));
		stonesLabel.setText("");
		stonesLabel.setForeground(java.awt.Color.black);
		this.add(stonesLabel,
                new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 0, 10),
                        0, 0));

		linesLabel.setPreferredSize(new Dimension(100, 19));
		linesLabel.setText("");
		linesLabel.setForeground(java.awt.Color.black);
		this.add(linesLabel,
                new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 0, 10),
                        0, 0));

		scoreLabel.setPreferredSize(new Dimension(100, 19));
		scoreLabel.setText("");
		scoreLabel.setForeground(java.awt.Color.black);
		this.add(scoreLabel,
                new GridBagConstraints(1, 5, 1, 1, 0.0, 1.0,
                        GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE,
                        new Insets(5, 0, 0, 10),
                        0, 0));

		//buttons for applet mode
		if (buttons) {
			this.buttonStart = new JButton("Start");
			this.add(this.buttonStart,
	                new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
	                        GridBagConstraints.NORTHWEST,
	                        GridBagConstraints.HORIZONTAL,
	                        new Insets(5, 0, 0, 10),
	                        0, 0));
			this.buttonStart.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					newGame(false);
				}
			});
			this.buttonDemo = new JButton("Demo");
			this.add(this.buttonDemo,
	                new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
	                        GridBagConstraints.NORTHWEST,
	                        GridBagConstraints.HORIZONTAL,
	                        new Insets(5, 0, 0, 10),
	                        0, 0));
			this.buttonDemo.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					newGame(true);
				}
			});
			this.buttonHighscore = new JButton("Highscores");
			this.add(this.buttonHighscore,
	                new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
	                        GridBagConstraints.NORTHWEST,
	                        GridBagConstraints.HORIZONTAL,
	                        new Insets(5, 0, 10, 10),
	                        0, 0));		
			this.buttonHighscore.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showHighScores();
				}
			});
		}
		
		//this.setSize(playPanel.pixelWidth + previewPanel.pixelWidth + 30, playPanel.pixelHeight + 20);
		this.setVisible(true);

		this.setName("HextrisPane");
		
		playPanel.getBoard().drawPlayField();
		gameOver = true;
		setFocusable(true);

	}
	
	/**
	 * reacts on keystrokes and moves the stone left, right and down.
	 * rotates the stone.
	 * configured keys have higher priority
	 * 
	 * @param e
	 */
	protected void gameKeyPressed(KeyEvent e) 
	{
		int kc = e.getKeyCode();
		
		if (gameOver || demo) return;
		if (this.currentStone==null) return;

		if (kc==ctx.getKeys()[0]) {
			this.currentStone.moveStone(Stone.MOVE_LEFT);
			playPanel.repaint();
		} else if (kc==ctx.getKeys()[1]) {
			this.currentStone.moveStone(Stone.MOVE_RIGHT);
			playPanel.repaint();
		} else if (kc==ctx.getKeys()[2]) {
			this.currentStone.moveStone(Stone.ROTATE_LEFT);
			playPanel.repaint();
		} else if (kc==ctx.getKeys()[3]) {
			this.currentStone.moveStone(Stone.ROTATE_RIGHT);
			playPanel.repaint();
		} else if (kc==ctx.getKeys()[4]) {
			if (this.action == NONE) {
				this.action = MOVE_DOWN;
				moverThread.interrupt();
			}
		} else if (kc==ctx.getKeys()[5]) {
			this.action = FALL_DOWN;
			moverThread.interrupt();
		} else if (kc == KeyEvent.VK_LEFT) {
			this.currentStone.moveStone(Stone.MOVE_LEFT);
			playPanel.repaint();
		} else if (kc == KeyEvent.VK_RIGHT) {
			this.currentStone.moveStone(Stone.MOVE_RIGHT);
			playPanel.repaint();
		} else if (kc == KeyEvent.VK_UP) {
			this.currentStone.moveStone(Stone.ROTATE_RIGHT);
			playPanel.repaint();
		} else if (kc == KeyEvent.VK_DOWN) {
			if (this.action == NONE) {
				this.action = MOVE_DOWN;
				moverThread.interrupt();
			}
		} else if (kc == KeyEvent.VK_SPACE) {
			this.action = FALL_DOWN;
			moverThread.interrupt();
		}
	} 

	/**
	 * Starts a new game
	 * 
	 * Resets the score, clears the board and creates a new stone.
	 * Starts the moverThread.
	 *
	 */
	public void newGame(boolean demo) 
	{
		int option = 0;
		
		 if (demo) {
			 this.severity = 1;
			 this.setLevel(7);
		 } else {
			 option = JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(this), 
						this.getStartMsg(), 
						"New Game",JOptionPane.DEFAULT_OPTION,
		                JOptionPane.QUESTION_MESSAGE, null,
		                new String[] {"Cancel","Start"}, "Start");
			 this.grabFocus();
			 if (option!=1) return;
			 this.severity = severityCB.getSelectedIndex();
			 setLevel(levelCB.getSelectedIndex()+1);
		 }

		 this.demo = demo;
		 setStones(0);	
		 setLines(0);
		 setScore(0);
		 if (nextStone!=null) nextStone.place(false);
		 playPanel.getBoard().drawPlayField();
		 playPanel.setGameOver(false);
		 nextStone = new Stone(previewPanel.getBoard(),severity);
		 createNextStone();
		 playPanel.repaint();
		 gameOver=false;
		 moverThread = new Thread ( this ) ;
		 moverThread.start();
		 this.grabFocus();
	}
	
	/**
	 * Creates a new randoem stone and places it at the top of the board.
	 * 
	 * @return
	 */
	private void createNextStone() 
	{	
		currentStone=new Stone(nextStone,playPanel.getBoard());
		currentStone.setPosition((this.playPanel.getPanelWidth()-5)/2, -1);
		
		if (currentStone.mayPlace(currentStone.getPosition().x,currentStone.getPosition().y)) {
			nextStone.place(false);
			nextStone=new Stone(previewPanel.getBoard(),severity);
			nextStone.setPosition(0, 0);
			nextStone.place(true);
			currentStone.place(true);
			previewPanel.repaint();
			playPanel.repaint();
			if (this.demo) {
				try {Thread.sleep(50);} catch (InterruptedException iex) {}
				this.currentStone.place(false);
				int[] bp = this.currentStone.getBestPosition();
				this.currentStone.place(true);
				
				for (int i=0;i<bp[1];i++) {
					this.currentStone.moveStone(Stone.ROTATE_LEFT);
					this.playPanel.repaint();	
					try {Thread.sleep(50);} catch (InterruptedException iex) {}
				}
				this.currentStone.place(false);
				this.currentStone.setPosition(bp[0], -1);
				this.currentStone.place(true);
				int dx = bp[0] - this.currentStone.getPosition().x;
				for (int i = 0; Math.abs(dx)>i; i++) {
					this.currentStone.moveStone(dx>0?Stone.MOVE_RIGHT:Stone.MOVE_LEFT);
					this.playPanel.repaint();
					try {Thread.sleep(50);} catch (InterruptedException iex) {}
				}				
			}
		} else this.gameOver();
	}	
	
	/**
	 * try to move down the stone. places the stone one line below it´s current position if possible.
	 * if the stone hits a filled block the board is cleared of full lines and stone is released to the board.
	 * @return true if the stone was placed succesfully
	 */
	private synchronized boolean moveDown() 
	{
		if (currentStone.moveStone(Stone.MOVE_DOWN)) {
			this.playPanel.repaint();
			return true;
		} else {
			this.releaseCurrentStone();
			addLines(this.playPanel.removeFullLines());
			createNextStone();			
			return false;
		}
	}
	
	/**
	 * moves the stone down, until he hits another stone
	 *
	 */
	private void fallDown()
	{
		boolean canMove = this.moveDown();
		while (canMove && !gameOver) {
			this.addScore((severity+1) * 2 * getLevel());
			try {Thread.sleep(5);} 
			catch (InterruptedException e1) {System.out.print(e1.getStackTrace());}
			canMove = this.moveDown();
		}
	}
	
	/**
	 * called when the game is over
	 * checks if the player gets into the highscore list. if yes the name is queried and the score added.
	 * stops the moverThread.
	 */
	public void gameOver() 
	{
		this.moverThread = null;
		this.playPanel.setGameOver(true);
		if (!gameOver && !this.demo && ScoreList.read()) {
			int place = ScoreList.getPlace(score);
			if (place<=ScoreList.getMaxSize()) {
				String playerName=JOptionPane.showInputDialog("You are #"+place+" in the highscore list.\nPlease enter your name:","");
				if (playerName!=null && playerName.trim().length()>0) {
					ScoreList.upload(new Score(playerName.trim().substring(0, Math.min(20, playerName.trim().length())),score));
					this.showHighScores();
				}
			}
		}
		this.gameOver = true;
	}

	/**
	 * the curren stone is released to the board and thus can not be moved anymore
	 *
	 */
	private void releaseCurrentStone()
	{
		this.currentStone = null;
		incStones();			
	}
	
	
	private int getLevel() 
	{
		return level;
	}

	private void setLevel(int i) 
	{
		level = i;
		levelLabel.setText("Level: "+level);
	}

	public void setMoverThread(Thread thread) 
	{
		moverThread = thread;
	}

	/**
	 * increases the score varianle and label
	 * @param s
	 */
	private void addScore(int s)
	{
		setScore(score + s);
	}

	private void setScore(int s)
	{
		score = s;
		scoreLabel.setText("Score: " + score);
	}
	
	/**
	 * increases the stones variable and sets the according score 
	 *
	 */
	private void incStones()
	{
		addScore((severity+1) * 10 * getLevel());
		setStones(++stones);
		if (stones>20*level && level<10) setLevel(getLevel()+1); 
	}

	private void setStones(int s)
	{
		stones = s;
		stonesLabel.setText("Stones: " + this.stones);
	}
	
	/**
	 * increases the lines variable and sets the according score 
	 * @param l
	 */
	private void addLines(int l)
	{
		addScore((severity+1) * 100 * level * l*l);
		setLines(lines+=l);
	}
	
	private void setLines(int l)
	{
		lines = l;
		linesLabel.setText("Lines: " + this.lines);				
	}

	public static String getVersion()
	{
		return version;
	}
	
	/**
	 * show the highscores
	 *
	 */
	public void showHighScores()
	{
		//prepare jta
		JTextArea jta = new JTextArea();
		jta.setFont(new java.awt.Font("Courier",0,12));
		jta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLoweredBevelBorder(), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		jta.setEditable(false);
		JScrollPane jsp = new JScrollPane();
		jsp.setPreferredSize(new java.awt.Dimension(200,300));
		jsp.getViewport().add(jta);
		
		//read highscores from server and write into jta
		if (ScoreList.read()) {
			ArrayList<Score> scoreList = ScoreList.getScoreList();
			for (int i = 0; i<scoreList.size(); i++) {
				String pos = new Integer(i+1).toString();
				jta.append(pos + "." + "   ".substring(pos.length()) + scoreList.get(i).toString() + "\n");
			}
		} else {
			jta.setText("Could not read highscore list.\n" +
					"Most certainly you are not\n" +
					"connected to the internet\n" +
					"or the server is down.");
		}
		jta.setCaretPosition(0);

		JOptionPane.showMessageDialog(JOptionPane.getFrameForComponent(this), 
				jsp,
				"Hextris Highscores",
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * the dialog when game is started containing level and severity selection
	 * @return
	 */
	private Object[] getStartMsg()
	{
		if (this.startMsg==null) {
			severityCB = new JComboBox();
			severityCB.addItem("Beginner");
			severityCB.addItem("Medium");
			severityCB.addItem("Expert");
			severityCB.setSelectedIndex(1);
			levelCB = new JComboBox();
			for (int i=1; i<=10; i++) levelCB.addItem(new Integer(i)); 
			this.startMsg = new Object[] {
					new JLabel("Severity:"),
					severityCB,
					new JLabel("Start level:"),
					levelCB};
		}
		
		return this.startMsg;
	}

	public static void main(String[] args) 
	{
		new MainFrame(new Hextris(false));
	}	

} 
