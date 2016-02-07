/*
*Author: Cameron Ekblad
*UPI: cekb635
*Date: 27/5/2011
*This is the main class for the game Mission: Infection Prevention! Contained in this class are most of the general mechanics of the game: various timers to control
*player and enemy movement, enemy respawning and firing delay, the array containing the enemies defined by the Zombie class, the borders of the playing-area and the
*sounds and graphics. It also contains methods for dealing with input from the user in the form of key presses, mouse movements and mouse clicks, and interaction
*between the player and the enemies (i.e. computing whether an enemy has been shot or come into contact with the player), and how to respond to these situations.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.applet.*;
import java.net.*;
import java.awt.geom.*;

public class A3JPanel extends JPanel implements MouseMotionListener, KeyListener, ActionListener, MouseListener {
	public static final int MAX_NUMBER_OF_ZOMBIES = 10;
	public static final char UP = 'w';
	public static final char DOWN = 's';
	public static final char LEFT = 'a';
	public static final char RIGHT = 'd';
		
	private boolean newInstance;
	private Font header;
	private Font subHeader;
	private Font mainText;
	private Font difficultyMode;
	private Font killCounter;
	private Line2D.Double topBorder;
	private Line2D.Double leftBorder;
	private Line2D.Double bottomBorder;
	private Line2D.Double rightBorder;	
	private Crosshair c;
	private Player player;
	private Timer upwardMovementTimer;
	private Timer downwardMovementTimer;
	private Timer leftMovementTimer;
	private Timer rightMovementTimer;
	private Timer zombieTimer;
	private Timer zombieRespawnTimer;
	private Timer firingDelay;
	private Timer reloadTimer;
	private char pressedKey;
	private boolean hasFired;
	private Zombie[] zombies;
	private AudioClip bgMusic;
	private AudioClip shotSound;
	private AudioClip reloadSound;
	private AudioClip zombieDies;
	private AudioClip playerDies;
	private double zombieSpeedIncreaseFactor;
	private boolean hardcoreMode;
	private boolean easyMode;
	private boolean normalMode;
	private boolean inFreezeMoment;
	
 	public A3JPanel() {
		setBackground(new Color(70, 30, 10));
		header = new Font("SansSerif", Font.BOLD, 30);
		subHeader = new Font("SansSerif", Font.BOLD, 22);
		mainText = new Font("SansSerif", 12, 12);
		difficultyMode = new Font("Dialog", Font.BOLD, 36);
		killCounter = new Font("Monospaced", Font.BOLD, 20);
		topBorder = new Line2D.Double(0.0, 0.0, 600.0, 0.0);
		bottomBorder = new Line2D.Double(0.0, 600.0, 600.0, 600.0);
		leftBorder = new Line2D.Double(0.0, 0.0, 0.0, 600.0);
		rightBorder = new Line2D.Double(600.0, 0.0, 600.0, 600.0);
		c = new Crosshair();
		player = new Player();
		upwardMovementTimer = new Timer(30, this);
		downwardMovementTimer = new Timer(30, this);
		leftMovementTimer = new Timer(30, this);
		rightMovementTimer = new Timer(30, this);
		zombieTimer = new Timer(30, this);
		zombieRespawnTimer = new Timer(3000, this);
		addMouseMotionListener(this);
		addKeyListener(this);
		addMouseListener(this);
		zombieSpeedIncreaseFactor = 0.15;
		zombies = new Zombie[MAX_NUMBER_OF_ZOMBIES];
		for(int i = 0; i < zombies.length; i++){
			zombies[i] = new Zombie(player.getMiddle(), zombieSpeedIncreaseFactor);
		}
		bgMusic = loadSound("MIP Theme.mid");
		shotSound = loadSound("shot.wav");
		reloadSound = loadSound("pump.wav");
		zombieDies = loadSound("hit.wav");
		playerDies = loadSound("dammit.wav");
		bgMusic.loop();
		newInstance = true;
	}
	
	public void actionPerformed(ActionEvent e){
		int zombieSpawnCount = 0;
		
		if(!(easyMode) && !(hardcoreMode)){
			normalMode = true;
		}
		
		if(newInstance){
			if(hardcoreMode){
				firingDelay = new Timer(2000, this);
				reloadTimer = new Timer(1500, this);
			}
			else if(easyMode){
				firingDelay = new Timer(500, this);
				reloadTimer = new Timer(100, this);
			}
			else if(normalMode){
				firingDelay = new Timer(1500, this);
				reloadTimer = new Timer(1000, this);
			}
			newInstance = false;
			inFreezeMoment = true;
		}
		
		else if(inFreezeMoment){
			repaint();
			inFreezeMoment = false;
		}
		
		else {
			zombieTimer.start();
			zombieRespawnTimer.start();
			
			if(e.getSource() == upwardMovementTimer && !(player.playerIsDead())){
				player.moveUp(topBorder);
				repaint();
				player.updatePlayerArea();
			}
			if(e.getSource() == downwardMovementTimer && !(player.playerIsDead())){
				player.moveDown(bottomBorder);
				repaint();
				player.updatePlayerArea();
			}
			if(e.getSource() == leftMovementTimer && !(player.playerIsDead())){
				player.moveLeft(leftBorder);
				repaint();
				player.updatePlayerArea();
			}
			if(e.getSource() == rightMovementTimer && !(player.playerIsDead())){
				player.moveRight(rightBorder);
				repaint();
				player.updatePlayerArea();
			}
			
			if(e.getSource() == zombieTimer && !(player.playerIsDead())){
				for(int i = 0; i < zombies.length; i++){
					if(!(zombies[i].zombieIsDead())){
						zombies[i].move(player.getMiddle());
					}
				}
				repaint();
				for(int i = 0; i < zombies.length; i++){
					if(zombies[i].getZombieArea().intersects(player.getPlayerArea()) && !(zombies[i].zombieIsDead())){
						playerDies.play();
						player.die();
						zombieTimer.stop();
						zombieRespawnTimer.stop();
					}
				}
			}
			if(e.getSource() == zombieRespawnTimer && !(player.playerIsDead())){
				for(int i = 0; i < zombies.length; i++){
					if(zombies[i].zombieIsDead()){
						zombies[i] = new Zombie(player.getMiddle(), zombieSpeedIncreaseFactor, player.getZombieKills() * zombieSpeedIncreaseFactor);
						if(hardcoreMode){
							zombieSpawnCount++;
							if(zombieSpawnCount == 3){
								break;
							}
						}
						else {
							break;
						}
					}
				}
			}
			if(e.getSource() == firingDelay){
				player.playerCanFire();
				firingDelay.stop();
			}
			if(e.getSource() == reloadTimer && !(player.playerIsDead())){
				reloadSound.play();
				reloadTimer.stop();
			}
		}
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyChar() == '1'){
			zombieSpeedIncreaseFactor = 0.1;
			for(int i = 0; i < zombies.length; i++){
				zombies[i] = new Zombie(player.getMiddle(), zombieSpeedIncreaseFactor);
			}
			zombieRespawnTimer.setDelay(6000);
			hardcoreMode = false;
			normalMode = false;
			easyMode = true;
			repaint();
		}
		
		else if(e.getKeyChar() == '3'){
			zombieSpeedIncreaseFactor = 0.2;
			for(int i = 0; i < zombies.length; i++){
				zombies[i] = new Zombie(player.getMiddle(), zombieSpeedIncreaseFactor);
			}
			zombieRespawnTimer.setDelay(2000);
			easyMode = false;
			normalMode = false;
			hardcoreMode = true;
			repaint();
		}
		
		else if(e.getKeyChar() == '2'){
			zombieSpeedIncreaseFactor = 0.15;
			for(int i = 0; i < zombies.length; i++){
				zombies[i] = new Zombie(player.getMiddle(), zombieSpeedIncreaseFactor);
			}
			zombieRespawnTimer.setDelay(3000);
			hardcoreMode = false;
			easyMode = false;
			normalMode = true;
			repaint();
		}
		
		else {
		
			if(e.getKeyChar() == UP){
				upwardMovementTimer.start();
			}
			if(e.getKeyChar() == DOWN){
				downwardMovementTimer.start();
			}
			if(e.getKeyChar() == LEFT){
				leftMovementTimer.start();
			}
			if(e.getKeyChar() == RIGHT){
				rightMovementTimer.start();
			}
			
		}
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyChar() == UP){
			upwardMovementTimer.stop();
		}
		if(e.getKeyChar() == DOWN){
			downwardMovementTimer.stop();
		}
		if(e.getKeyChar() == LEFT){
			leftMovementTimer.stop();
		}
		if(e.getKeyChar() == RIGHT){
			rightMovementTimer.stop();
		}
	}
	
	public void mouseMoved(MouseEvent e){
		c.setCentre(e.getX(), e.getY());
		repaint();
	}
	
	public void mousePressed(MouseEvent e){
		if((player.canPlayerFire() && !(player.playerIsDead())) && (!(newInstance) && zombieTimer.isRunning())){
			shotSound.play();
			player.fire(c.getCentre());
			for(int i = 0; i < zombies.length; i++){
				if(zombies[i].getZombieArea().intersectsLine(player.getShot()) && !(zombies[i].zombieIsDead())){
					zombieDies.play();
					zombies[i].die();
					player.addZombieKill();
					for(int j = 0; j < zombies.length; j++){
						zombies[j].speedUp();
					}
				}
			}
			hasFired = true;
			player.playerCannotFire();
			firingDelay.start();
			reloadTimer.start();
			repaint();
		}
	}
	
	public void mouseReleased(MouseEvent e){
		hasFired = false;
		repaint();
	}

   	public void paintComponent(Graphics g){
 	    super.paintComponent(g);
		if(newInstance){
			drawTitleScreen(g);
			
			if(easyMode){
				g.setColor(Color.RED);
				g.setFont(difficultyMode);
				g.drawString("1", 200, 550);
				g.drawRect(195, 520, 30, 35);
				g.setFont(mainText);
				g.drawString("easy", 198, 570);
			}
			else if(normalMode){
				g.setColor(Color.RED);
				g.setFont(difficultyMode);
				g.drawString("2", 275, 550);
				g.drawRect(270, 520, 30, 35);
				g.setFont(mainText);
				g.drawString("Normal", 265, 570);
			}
			else if(hardcoreMode){
				g.setColor(Color.RED);
				g.setFont(difficultyMode);
				g.drawString("3", 350, 550);
				g.drawRect(345, 520, 30, 35);
				g.setFont(mainText);
				g.drawString("HARDCORE", 324, 570);
			}
			
		}
		
		else {
			if(hasFired){
				player.drawShot(g, c.getCentre());
			}
			
			for(int i = 0; i < zombies.length; i++){
				if(!(zombies[i].zombieIsDead())){
					zombies[i].draw(g);
				}
			}
			
			player.draw(g);
			c.draw(g);
			drawKillCounter(g);
			if(player.playerIsDead()){
				g.setFont(header);
				g.setColor(Color.BLACK);
				g.drawString("GAME OVER", 200, 100);
			}
		}
	}
	
	public void drawKillCounter(Graphics g){
		g.setColor(Color.RED);
		g.setFont(killCounter);
		g.drawString("Kills: " + (int)(player.getZombieKills()), 0, 600);
	}
	
	public void drawTitleScreen(Graphics g){
		g.setColor(Color.red);
		g.setFont(header);
		g.drawString("Mission: Infection Prevention! (MIP!)", 50, 100);
		g.setFont(subHeader);
		g.drawString("by cekb635", 100, 140);
		g.setFont(mainText);
		g.drawString("Welcome to the apocalypse! In this game, the objective is to hold off the horde for as long as possible.", 25, 200);
		g.drawString("To do this, take out the oncoming zombies with your shotgun. But beware; your shotgun is pump-action,", 25, 250);
		g.drawString("and takes a moment to reload, and each time you take one out, the zombies get faster.", 25, 275);
		g.drawString("Oh, and they don't stay down... TIP: Try and trap multiple zombies in your firing line by aiming past them!", 25, 300);
		g.drawString("Use the W, A, S and D keys to move your player around the screen, aim using the mouse and click to fire!", 25, 350);
		g.drawString("Press 1 for easy-mode if you're having trouble! Your shotgun reloads faster and the zombies are slower,", 25, 400);
		g.drawString("and respawn less frequently. Press 2 to switch back to normal mode, or...", 25, 425);
		g.drawString("Press 3 for hardcore-mode: your shotgun is a little rusty, and loads a little slower... Plus, the zombies", 25, 450);
		g.drawString("are faster, and come back to life very quickly!", 25, 475);
		g.drawLine(25, 452, 175, 452);
		g.drawString("Press W, A, S or D to begin...", 225, 500);
		g.setFont(difficultyMode);
		g.setColor(Color.black);
		g.drawString("1", 200, 550);
		g.drawRect(195, 520, 30, 35);
		g.drawString("2", 275, 550);
		g.drawRect(270, 520, 30, 35);
		g.drawString("3", 350, 550);
		g.drawRect(345, 520, 30, 35);
		g.setFont(mainText);
		g.drawString("easy", 198, 570);
		g.drawString("Normal", 265, 570);
		g.drawString("HARDCORE", 324, 570);
	}
	
	public void mouseDragged(MouseEvent e){}
	
	public void keyTyped(KeyEvent e){}
	
	public void mouseClicked(MouseEvent e){}
	
	public void mouseEntered(MouseEvent e){}
	
	public void mouseExited(MouseEvent e){}
	
	private AudioClip loadSound(String fileName) {
		URL url = getClass().getResource(fileName);
		return Applet.newAudioClip(url);
	}
}