package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;

import particle_force.SingleForce;

public class UI {
	SimulationPanel sp;
	Config conf = Config.getInstance();
	
	Graphics2D g2;
	
	Font arial_20;
	Font arial_40;
	
	Color subWindowBgdColor = new Color(0,0,0,200);
	Color subWindowBorderColor = new Color(255,255,255,200);
	Stroke subWindowBorderStroke = new BasicStroke(5);

	final int cameraScreenCenterXForOptions = ((conf.optionsWidth + 100) + (conf.screenWidth-50))/2;
	final double desiredWidth = (conf.screenWidth - (conf.optionsWidth + 100) - 50);
	final double cameraScaleForOptions = Math.min(desiredWidth/conf.envWidth, (conf.screenHeight-50)/conf.envHeight);
	
//	slider
	
	public UI(SimulationPanel sp) {
		this.sp = sp;
		
		arial_20 = new Font("Arial", Font.PLAIN, 20);
		arial_40 = new Font("Arial", Font.PLAIN, 40);
	}
	
	public void setCameraToOptionsScaling() {
		sp.camera.setFramePosition(cameraScreenCenterXForOptions, conf.screenHeight/2);
		sp.camera.setZoom(cameraScaleForOptions);
	}
	
	public void setCameraToStandardScaling() {
		sp.camera.setFramePosition(conf.cameraScreenCenterX, conf.cameraScreenCenterY);
		sp.camera.setZoom(conf.cameraDefaultZoom);
	}
	
	
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		
		if(conf.drawFPS) {
			drawFPS(conf.screenWidth - 100, 50);
		}
		
		if(sp.state.optionsOpen()) {
			drawOptionsScreen();
		}
	}
	
	public void drawSubWindow(int x, int y, int width, int height)
	{
		g2.setColor(subWindowBgdColor);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		g2.setColor(subWindowBorderColor);
		g2.setStroke(subWindowBorderStroke);
		g2.drawRoundRect(x, y, width, height, 35, 35);
	}
	
	public void drawOptionsScreen() {
		drawSubWindow(conf.optionsFrameX, conf.optionsFrameY, conf.optionsWidth, conf.optionsHeight);	
		
		int leftRulerFrameX = conf.optionsFrameX + 25;
		g2.setColor(subWindowBorderColor);
		g2.setFont(arial_20);
		int textX = leftRulerFrameX;
		int textY = conf.optionsFrameY + 50;
		g2.drawString("Options", textX, textY);
		
		drawForceMatrix(leftRulerFrameX, textY + 25, conf.optionsWidth/2, conf.optionsWidth/2);
	}
	
	public void drawForceMatrix(int screenX, int screenY, int width, int height) {
		int tileSize = width/conf.forceMatrix.length;
		
		int tileScreenX;
		int tileScreenY;
		
		for (int i = 0; i < conf.forceMatrix.length; i++) {
			SingleForce[] forceRow = conf.forceMatrix[i];
			for (int j = 0; j < forceRow.length; j++) {
				SingleForce singleForce = forceRow[j];
				tileScreenX = screenX + i*tileSize;
				tileScreenY = screenY + j*tileSize;
				g2.setColor(singleForce.color);
				g2.fillRect(tileScreenX, tileScreenY, tileSize, tileSize);
				g2.setColor(Color.black);
				g2.drawRect(tileScreenX, tileScreenY, tileSize, tileSize);

			}
			
		}
	}
	
	public void drawFPS(int screenX, int screenY) {
		g2.setColor(Color.white);
		g2.setFont(arial_20);
		g2.drawString("FPS: " + sp.FPS, screenX, screenY);
	}
	
}
