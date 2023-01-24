package particle;

import java.awt.Color;
import java.awt.Graphics2D;

import camera.Camera;
import hashgrid.ISpatial;
import main.Config;
import particle_force.MultiForce;
import vector.Vector2D;

public class Particle implements ISpatial {	
	private Config conf = Config.getInstance();
	
	private Vector2D pos;
	private Vector2D vel;
	private Vector2D acc;
	
	public MultiForce force;
	
	public Color color;
	
	public Particle(MultiForce force, Color color) {
		super();
		this.force = force;
		
		pos = new Vector2D();
		vel = new Vector2D();
		acc = new Vector2D();
		
		this.color = color;
		
	} 
	
	public double getX() {
		return pos.x;
	}
	
	public double getY() {
		return pos.y;
	}
	
	public void setX(double x) {
		this.pos.x = x;
	}
	
	public void setY(double y) {
		this.pos.y = y;
	}
	
	public void setForce(MultiForce force) {
		this.force = force;
	}

	public Vector2D getPos() {
		return pos;
	}

	public void setPos(Vector2D pos) {
		this.pos = pos;
	}

	public Vector2D getVel() {
		return vel;
	}

	public void setVel(Vector2D vel) {
		this.vel = vel;
	}

	public void update(double dt) {
		this.acc.multiply(dt);
		
		this.vel.add(acc);
		
		Vector2D translation = vel.getMultiplied(dt);
		this.pos.add(translation);
		if(pos == null) {
			System.out.println(acc);
		}

		this.acc.setZero();
	}
	
	public void draw(Graphics2D g2, Camera c) {		
		int radius = (int) c.zoomElongation(conf.particleDrawSize);
		int screenX = c.getFrameX(pos.x) - radius;
		int screenY = c.getFrameY(pos.y) - radius;
		
				
		g2.setColor(color);
		g2.fillOval(screenX, screenY,2*radius, 2*radius);
	}
	
	public void drawForce(Graphics2D g2, Camera c) {
		int radius = (int) c.zoomElongation(force.maxRadius);
		int screenX = c.getFrameX(pos.x) - radius;
		int screenY = c.getFrameY(pos.y) - radius;

		g2.setColor(conf.forceColor);
		g2.fillOval(screenX, screenY,2*radius, 2*radius);
	}
	
	public void highlight(Graphics2D g2, Camera c, Color clr) {
		int radius = c.zoomElongation(conf.particleDrawSize + 10);
		int screenX = c.getFrameX(pos.x) - radius;
		int screenY = c.getFrameY(pos.y) - radius;
		
				
		g2.setColor(clr);
		g2.fillOval(screenX, screenY,2*radius, 2*radius);
	}
	
	public void applyForce(Vector2D force) {
		final double MASS = 1;
		force.divide(MASS);
		acc.add(force);
	}
}
