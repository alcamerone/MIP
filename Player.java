/*
*Author: Cameron Ekblad
*UPI: cekb635
*Date: 27/5/2011
*This is the Player class, which represents the user's avatar in the game. It contains basic information relating to the player, such as movement speed, 
*position on screen and size, and also contains methods to move the character around the screen, fire, and (unfortunately) die. It also contains details on how 
*the character is meant to be drawn, and various accessor and mutator methods which are used by the A3JPanel class to gain and manipulate information 
*relating to the player.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

public class Player{
	
	public static final char UP = 'w';
	public static final char DOWN = 's';
	public static final char LEFT = 'a';
	public static final char RIGHT = 'd';
	public static final int SPEED = 2;
	public static final int SIZE = 30;
	
	private Point middle;
	private Rectangle playerArea;
	private boolean isDead;
	private double zombieKills;
	private boolean canFire;
	private Line2D.Double shot;
	
	public Player(){
		middle = new Point(300,300);
		playerArea = new Rectangle(middle.x - SIZE/2, middle.y - SIZE/2, SIZE, SIZE);
		zombieKills = 0;
		canFire = true;
	}
	
	public void moveUp(Line2D.Double topBorder){
		if(!(playerArea.intersectsLine(topBorder))){
			middle.y = middle.y - SPEED;
		}
	}
	
	public void moveDown(Line2D.Double bottomBorder){
		if(!(playerArea.intersectsLine(bottomBorder))){
			middle.y = middle.y + SPEED;
		}
	}
	
	public void moveLeft(Line2D.Double leftBorder){
		if(!(playerArea.intersectsLine(leftBorder))){
			middle.x = middle.x - SPEED;
		}
	}
	
	public void moveRight(Line2D.Double rightBorder){
		if(!(playerArea.intersectsLine(rightBorder))){
			middle.x = middle.x + SPEED;
		}
	}
	
	public void updatePlayerArea(){
		playerArea = new Rectangle(middle.x - SIZE/2, middle.y - SIZE/2, SIZE, SIZE);
	}
	
	public void die(){
		isDead = true;
	}
	
	public void fire(Point p){
		shot = new Line2D.Double(middle, p);
	}
	
	public void drawShot(Graphics g, Point p){
		g.setColor(Color.red);
		g.drawLine(middle.x, middle.y, p.x, p.y);
	}
	
	public void draw(Graphics g){
		g.setColor(new Color(211, 175, 142));
		g.fillOval(middle.x - SIZE/2, middle.y - SIZE/2, SIZE, SIZE);
	}
	
	public void addZombieKill(){
		zombieKills++;
	}
	
	public void playerCanFire(){
		canFire = true;
	}
	
	public void playerCannotFire(){
		canFire = false;
	}
	
	public Point getMiddle(){
		return middle;
	}
	
	public Rectangle getPlayerArea(){
		return playerArea;
	}
	
	public boolean playerIsDead(){
		return isDead;
	}
	
	public double getZombieKills(){
		return zombieKills;
	}
	
	public boolean canPlayerFire(){
		return canFire;
	}
	
	public Line2D.Double getShot(){
		return shot;
	}
}