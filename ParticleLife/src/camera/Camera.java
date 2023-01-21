package camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.Config;
import main.SimulationPanel;
import particle.Particle;

public class Camera {
	final private Config conf = Config.getInstance();
	
	public double worldX = 0;
	public double worldY = 0;
	public int width;
	public int height;
	

	public Camera(SimulationPanel sp, int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setPosition(int x, int y) {
		this.worldX = x;
		this.worldY = y;
	}
	
	public double getCenterX() {
		return worldX + width/2;
	}
	
	public double getCenterY() {
		return worldY + height/2;
	}

	public void centerOn(double x, double y) {
		this.worldX = x - conf.screenWidth / 2;
		this.worldY = y - conf.screenHeight / 2;
	}

	public boolean sees(Particle p) {
		boolean outOfCameraView = (worldX) > p.getX() ||
				(worldX + width) < p.getX() ||
				(worldY) > p.getY() || 
				(worldY + height) < p.getY();
		return !outOfCameraView;
	}

	public void draw(Graphics2D g2) {
		g2.setColor(Color.BLUE);
		g2.setStroke(new BasicStroke(2));
		g2.drawRect(conf.screenWidth/2 - width/2, conf.screenHeight/2 - height/2, width, height);
		g2.drawLine(conf.screenWidth/2 - width/2, conf.screenHeight/2 - height/2, conf.screenWidth/2 + width/2, conf.screenHeight/2 + height/2);
		g2.drawLine(conf.screenWidth/2 - width/2, conf.screenHeight/2 + height/2, conf.screenWidth/2 + width/2, conf.screenHeight/2 - height/2);

	}
}
