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
	public final int screenWidth = 1050;
	public final int screenHeight = 800;
	public final int optionsFrameX = 50;
	public final int optionsFrameY = 50;
	public final int optionsWidth = 300;
	public final int optionsHeight = screenHeight - 2*optionsFrameY;

	// SIM PARAMS
	public double simStep = 0.1;

	// FPS
	public int FPS = 60;
	public int UPF = 1; // updates per frame

	// ENV PARAMS
	public final double envWidth = 1000;
	public final double envHeight = 1000;
	public final boolean envWrapped = true;
	
	public double airResistanceCoef = 0.01;
	
	//MULTITHREADING
	public final int numberOfThreads = Runtime.getRuntime().availableProcessors();
	public final int taskSize = 300;

	// SHGRID PARAMS
	public double cellSize = 100;
	public int cols = (int) (envWidth / cellSize);
	public int rows = (int) (envHeight / cellSize);

	// PARTICLE PARAMS
	public final Color particleDefColor = Color.WHITE;
	public final double particleDrawSize = 10;

	// CAMERA
	public final int cameraWidth = 600;
	public final int cameraHeight = 600;
	public final int cameraScreenCenterX = screenWidth/2;
	public final int cameraScreenCenterY = screenHeight/2;
	public final double cameraDefaultZoom = Math.min((screenWidth-50)/envWidth,(screenHeight-50)/envHeight);

	// PARTICLE SET
	public int[] startAmounts = { 1800, 0, 0 };
	public SingleForce redOnRed = new SingleForce(25, 200, 10, -1, Color.RED);
	public SingleForce redOnGreen = new SingleForce(25, 50, 10, 1, Color.GREEN);
	public SingleForce redOnBlue = new SingleForce(25, 50, 10, 1, Color.BLUE);

	public SingleForce greenOnRed = new SingleForce(25, 100, 20, -1, Color.RED);
	public SingleForce greenOnGreen = new SingleForce(25, 50, 10, -1, Color.GREEN);
	public SingleForce greenOnBlue = new SingleForce(25, 50, 10, -1, Color.BLUE);
	
	public SingleForce blueOnRed = new SingleForce(25, 100, 20, 0, Color.RED);
	public SingleForce blueOnGreen = new SingleForce(25, 50, 10, 0, Color.GREEN);
	public SingleForce blueOnBlue = new SingleForce(25, 200, 10, -1, Color.BLUE);


	public final SingleForce[][] forceMatrix = { { redOnRed, redOnGreen, redOnBlue}, { greenOnRed, greenOnGreen, greenOnBlue}, {blueOnRed, blueOnGreen, blueOnBlue} };
	
	//DRAW TOGGLES
	public boolean drawForces = false;
	public boolean drawCamera = false;
	public boolean drawEnvBorder = true;
	public boolean drawSHGrid = false;

}
