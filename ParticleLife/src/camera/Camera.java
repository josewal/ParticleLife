package camera;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import main.Config;
import particle.Particle;

public class Camera {
	final private Config conf = Config.getInstance();

	public double worldCenterX = 0;
	public double worldCenterY = 0;
	public int frameCenterX = 0;
	public int frameCenterY = 0;
	public int width;
	public int height;
	public double zoom;

	public Camera() {
		setup();
	}

	public void setup() {
		width = conf.cameraWidth;
		height = conf.cameraHeight;
		frameCenterX = conf.cameraScreenCenterX;
		frameCenterY = conf.cameraScreenCenterY;
		zoom = conf.cameraDefaultZoom;
	}

	public void setWorldPosition(int x, int y) {
		this.worldCenterX = x;
		this.worldCenterY = y;
	}

	public void setFramePosition(int x, int y) {
		this.frameCenterX = x;
		this.frameCenterY = y;
	}

	public double getFrameCenterX() {
		return getFrameX(worldCenterX + width / 2);
	}

	public double getFrameCenterY() {
		return getFrameY(worldCenterY + height / 2);
	}

	public int getFrameX(double worldX) {
		return (int) ((worldX - this.worldCenterX) * zoom + frameCenterX);
	}

	public int getFrameY(double worldY) {
		return (int) ((worldY - this.worldCenterY) * zoom + frameCenterY);
	}

	public int zoomElongation(double dim) {
		return (int) (dim * zoom);
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public void focusOn(double x, double y) {
		this.worldCenterX = x;
		this.worldCenterY = y;
	}

	public boolean sees(Particle p) {
		boolean outOfCameraView = (worldCenterX) > p.getX() || (worldCenterX + width) < p.getX()
				|| (worldCenterY) > p.getY() || (worldCenterY + height) < p.getY();
		return !outOfCameraView;
	}

	public void draw(Graphics2D g2) {
		g2.setColor(Color.CYAN);
		g2.setStroke(new BasicStroke(2));
		int topLX = frameCenterX - width / 2;
		int topLY = frameCenterY - height / 2;
		g2.drawRect(topLX, topLY, width, height);
		g2.drawLine(topLX, topLY, frameCenterX + width / 2, frameCenterY + height / 2);
		g2.drawLine(topLX, frameCenterY + height / 2, frameCenterX + width / 2, topLY);

//		g2.setColor(Color.CYAN);
//		g2.drawRect((int) (topLX), (int) (topLY), (int) (width * 1/zoom), (int) (height * 1/zoom));
	}
}
