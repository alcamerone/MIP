/*
*Author: Cameron Ekblad
*UPI: cekb635
*Date: 27/5/2011
*This is the Zombie class, which represents the enemy in the game. This class contains information about the zombies, such as speed, the factor by which their
*speed increases if one is killed, the position and size on screen and whether the zombie is currently "dead" or not. It also contains a method for the zombies
*to move, which implements an algorithm designed to slightly randomise the zombies' movement pattern to make it more difficult to predict. This class, like the Player
*class, also contains a method for the zombies to die, and details of how the zombies should be drawn to the screen.
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Zombie{
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int SIZE = 24;
	
	private boolean isDead;
	private Point position;
	private Rectangle zombieArea;
	private double speed;
	private double speedIncreaseFactor;
	private Rectangle noSpawnArea;
	
	public Zombie(Point p, double newSpeedIncreaseFactor){
		isDead = false;
		noSpawnArea = new Rectangle(p.x - 100, p.y - 100, 200, 200);
		position = new Point((int)(Math.random() * 601), (int)(Math.random() * 601));
		while(noSpawnArea.contains(position)){
			position = new Point((int)(Math.random() * 601), (int)(Math.random() * 601));
		}
		zombieArea = new Rectangle(position.x - SIZE/2, position.y - SIZE/2, SIZE, SIZE);
		speedIncreaseFactor = newSpeedIncreaseFactor;
		speed = 1.0;
	}
	
	public Zombie(Point p, double newSpeedIncreaseFactor, double newSpeed){
		isDead = false;
		noSpawnArea = new Rectangle(p.x - 100, p.y - 100, 200, 200);
		position = new Point((int)(Math.random() * 601), (int)(Math.random() * 601));
		while(noSpawnArea.contains(position)){
			position = new Point((int)(Math.random() * 601), (int)(Math.random() * 601));
		}
		zombieArea = new Rectangle(position.x - SIZE/2, position.y - SIZE/2, SIZE, SIZE);
		speedIncreaseFactor = newSpeedIncreaseFactor;
		speed = 1.0 + newSpeed;
	}
	
	public void move(Point p){
		int xDirectionChance;
		int yDirectionChance;
		
		xDirectionChance = (int)(Math.random() * 23);
		yDirectionChance = (int)(Math.random() * 23);
		
		if(xDirectionChance >= 7 && p.x > position.x){
			position.x += speed;
		}
		else if(xDirectionChance >= 7 && p.x < position.x){
			position.x -= speed;
		}
		else if((xDirectionChance > 1 && xDirectionChance < 7) && p.x > position.x){
			position.x -= speed;
		}
		else if((xDirectionChance > 1 && xDirectionChance < 7) && p.x < position.x){
			position.x += speed;
		}
		
		if(yDirectionChance >= 7 && p.y > position.y){
			position.y += speed;
		}
		else if(yDirectionChance >= 7 && p.y < position.y){
			position.y -= speed;
		}
		else if((yDirectionChance > 1 && yDirectionChance < 7) && p.y > position.y){
			position.y -= speed;
		}
		else if((yDirectionChance > 1 && yDirectionChance < 7) && p.y < position.y){
			position.y += speed;
		}
		
		zombieArea = new Rectangle(position.x - SIZE/2, position.y - SIZE/2, SIZE, SIZE);
	}
	
	public Rectangle getZombieArea(){
		return zombieArea;
	}
	
	public boolean zombieIsDead(){
		return isDead;
	}
	
	public void die(){
		isDead = true;
	}
	
	public void speedUp(){
		speed = speed + speedIncreaseFactor;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.pink);
		g.fillOval(position.x - SIZE/2, position.y - SIZE/2, SIZE, SIZE);
		g.setColor(Color.black);
		g.drawOval(position.x - SIZE/2, position.y - SIZE/2, SIZE, SIZE);
		g.setColor(Color.red);
		g.drawLine(zombieArea.x + 6, zombieArea.y, zombieArea.x + 6, zombieArea.y + 15);
		g.drawLine(zombieArea.x + 18, zombieArea.y, zombieArea.x + 21, zombieArea.y + 5);
		g.drawLine(zombieArea.x + 21, zombieArea.y + 5, zombieArea.x + 18, zombieArea.y + 10);
		g.drawLine(zombieArea.x + 18, zombieArea.y + 10, zombieArea.x + 21, zombieArea.y + 15);
		g.drawLine(zombieArea.x + 21, zombieArea.y + 15, zombieArea.x + 18, zombieArea.y + 20);
	}
}