package particle;

import java.awt.Color;
import java.awt.Graphics2D;

import camera.Camera;
import hashgrid.ISpatial;
import vector.Vector2D;

public class Particle implements ISpatial {	
	private Vector2D pos;
	private Vector2D vel;
	private Vector2D acc;
	
	public ParticleForce force;
	
	private Color color;
	
	public Particle(ParticleForce force, Color color) {
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
	
	public void setForce(ParticleForce force) {
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
//		drawForce(g2, c);
		
		int screenX = (int) (pos.x - force.rMin/4 - c.worldX);
		int screenY = (int) (pos.y - force.rMin/4 - c.worldY);
				
		g2.setColor(color);
		g2.fillOval(screenX, screenY,(int) force.rMin/2,(int) force.rMin/2);
	}
	
	public void drawForce(Graphics2D g2, Camera c) {
		int screenX = (int) (pos.x - force.rMax - c.worldX);
		int screenY = (int) (pos.y - force.rMax - c.worldY);

		g2.setColor(force.midColor);
		g2.fillOval(screenX, screenY,(int) (2*force.rMax),(int) (2*force.rMax));
	}
	
	public void highlight(Graphics2D g2, Camera cam, Color clr) {
		int screenX = (int) (pos.x - force.rMin/4 - cam.worldX);
		int screenY = (int) (pos.y - force.rMin/4 - cam.worldY);

		g2.setColor(clr);
		g2.fillOval(screenX, screenY,(int) force.rMin/2,(int) force.rMin/2);

	}
	
	public void applyForce(Vector2D force) {
		final double MASS = 1;
		force.divide(MASS);
		acc.add(force);
	}
}
