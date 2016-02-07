/*
*Author: Cameron Ekblad
*UPI: cekb635
*Date: 27/5/2011
*This is the Crosshair class, which represents the point on screen at which the user is aiming. It is a simple class, with only one variable; its position on screen,
*and methods to access and change said. The setCentre method is only called by the MouseMotionListener in the A3JPanel class, which moves the crosshair to where the
*cursor is located on screen. It also contains the details used to draw a simple crosshair to the screen.
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Crosshair{
	
	private Point centre;
	
	public Crosshair(){
		centre = new Point(300, 300);
	}
	
	public void draw(Graphics g){
		g.setColor(Color.red);
		g.drawLine(centre.x, centre.y + 5, centre.x, centre.y + 12);
		g.drawLine(centre.x + 5, centre.y, centre.x + 12, centre.y);
		g.drawLine(centre.x, centre.y - 5, centre.x, centre.y - 12);
		g.drawLine(centre.x - 5, centre.y, centre.x - 12, centre.y);
	}
	
	public void setCentre(int x, int y){
		centre.x = x;
		centre.y = y;
	}
	
	public Point getCentre(){
		return centre;
	}
}