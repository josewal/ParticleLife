package main;

import java.awt.Color;

import particle_force.SingleForce;

public class Config {
	private static Config instance = null;

	private Config() {
	}

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();

		return instance;
	}

	// SCREEN PARAMS
	public final int screenWidth = 800;
	public final int screenHeight = 800;
	public final int optionsFrameX = 50;
	public final int optionsFrameY = 50;
	public final int optionsWidth = 300;
	public final int optionsHeight = screenHeight - 2*optionsFrameY;

	// SIM PARAMS
	public double simStep = 0.01;

	// FPS
	public int FPS = 60;
	public int UPF = 1; // updates per frame

	// ENV PARAMS
	public final double envWidth = 2000;
	public final double envHeight = 2000;
	public final boolean envWrapped = true;
	
	public double airResistanceCoef = 0.1;
	
	//MULTITHREADING
	public final int numberOfThreads = Runtime.getRuntime().availableProcessors();
//	public final int taskSize = 3000/numberOfThreads + 1;
	public final int taskSize = 5;

	// SHGRID PARAMS
	public double cellSize = 90;
	public int cols = (int) (envWidth / cellSize);
	public int rows = (int) (envHeight / cellSize);

	// PARTICLE PARAMS
	public final Color particleDefColor = Color.WHITE;
	public final Color forceColor = new Color(255,255,255,10);
	public final double particleDrawSize = 3;

	// CAMERA
	public final int cameraWidth = 600;
	public final int cameraHeight = 600;
	public final int cameraScreenCenterX = screenWidth/2;
	public final int cameraScreenCenterY = screenHeight/2;
	public final double cameraDefaultZoom = Math.min((screenWidth-50)/envWidth,(screenHeight-50)/envHeight);

	// PARTICLE SET
	public int[] startAmounts = { 2000, 0, 0, 2000};
	public SingleForce redOnRed = new SingleForce(0.9, 20, 5, 0, Color.RED);
	public SingleForce redOnGreen = new SingleForce(0.5, 50, 2, 2, Color.GREEN);
	public SingleForce redOnYellow = new SingleForce(0.3, 50, 1, -1, Color.YELLOW);
	public SingleForce redOnCyan = new SingleForce(0.3, 50, 1, -1, Color.CYAN);


	public SingleForce greenOnRed = new SingleForce(0.5, 50, 2, -1, Color.RED);
	public SingleForce greenOnGreen = new SingleForce(0.5, 50, 2, -0.1, Color.GREEN);
	public SingleForce greenOnYellow = new SingleForce(0.3, 51, 2, 3, Color.YELLOW);
	public SingleForce greenOnCyan = new SingleForce(0.3, 50, 1, -1, Color.CYAN);

	
	public SingleForce yellowOnRed = new SingleForce(0.3, 50, 1, -1, Color.RED);
	public SingleForce yellowOnGreen = new SingleForce(0.3, 51, 2, 0, Color.GREEN);
	public SingleForce yellowOnYellow = new SingleForce(0.3, 70, 2, 2, Color.YELLOW);
	public SingleForce yellowOnCyan = new SingleForce(0.3, 50, 1, -1, Color.CYAN);

	public SingleForce cyanOnRed = new SingleForce(0.3, 50, 1, 1, Color.RED);
	public SingleForce cyanOnGreen = new SingleForce(0.3, 51, 2, 0, Color.GREEN);
	public SingleForce cyanOnYellow = new SingleForce(0.3, 70, 2, 2, Color.YELLOW);
	public SingleForce cyanOnCyan = new SingleForce(0.9, 20, 5, 0, Color.CYAN);

	public final SingleForce[][] forceMatrix = { { redOnRed, redOnGreen, redOnYellow, redOnCyan}, { greenOnRed, greenOnGreen, greenOnYellow, greenOnCyan}, {yellowOnRed, yellowOnGreen, yellowOnYellow, yellowOnCyan}, 	{cyanOnRed, cyanOnGreen, cyanOnYellow, cyanOnCyan}
 };
	
	//DRAW TOGGLES
	public boolean drawFPS = true;
	public boolean drawForces = true;
	public boolean drawCamera = false;
	public boolean drawEnvBorder = false;
	public boolean drawSHGrid = true;

}
