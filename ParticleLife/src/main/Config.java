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
	public final int screenWidth = 2000;
	public final int screenHeight = 1400;
	public final int optionsFrameX = 50;
	public final int optionsFrameY = 50;
	public final int optionsWidth = 300;
	public final int optionsHeight = screenHeight - 2 * optionsFrameY;

	// SIM PARAMS
	public double simStep = 0.01;

	// FPS
	public int FPS = 30;
	public int UPF = 2; // updates per frame

	// ENV PARAMS
	public final double envWidth = 4000;
	public final double envHeight = 3000;
	public final boolean envWrapped = true;

	public double airResistanceCoef = 0.01;

	// SHGRID PARAMS
	public double cellSize = 1000;
	public int cols = (int) (envWidth / cellSize);
	public int rows = (int) (envHeight / cellSize);

	// MULTITHREADING
	public final int numberOfThreads = Runtime.getRuntime().availableProcessors();
//	public final int taskSize = 3000/numberOfThreads + 1;
	public final int taskSize = 100;

	// PARTICLE PARAMS
	public final Color particleDefColor = Color.WHITE;
	public final Color forceColor = new Color(255, 255, 255, 2);
	public final double particleDrawSize = 5;

	// CAMERA
	public final int cameraWidth = 600;
	public final int cameraHeight = 600;
	public final int cameraScreenCenterX = screenWidth / 2;
	public final int cameraScreenCenterY = screenHeight / 2;
	public final double cameraDefaultZoom = Math.min((screenWidth - 50) / envWidth, (screenHeight - 50) / envHeight);

	// PARTICLE SET
	public int[] startAmounts = { 1000, 0, 0, 0 };
	public SingleForce redOnRed = new SingleForce(0.7, 500, 10, -5, Color.RED);
	public SingleForce redOnGreen = new SingleForce(50. / 51., 51, 0, 0, Color.GREEN);
	public SingleForce redOnYellow = new SingleForce(25. / 50., 50, 0, -1, Color.YELLOW);
	public SingleForce redOnCyan = new SingleForce(25. / 50., 50, 1, -1, Color.CYAN);

	public SingleForce greenOnRed = new SingleForce(50. / 51, 51, 2, 3, Color.RED);
	public SingleForce greenOnGreen = new SingleForce(50. / 75, 75, 4, -1, Color.GREEN);
	public SingleForce greenOnYellow = new SingleForce(50. / 51, 51, 2, 3, Color.YELLOW);
	public SingleForce greenOnCyan = new SingleForce(25. / 50, 50, 1, -1, Color.CYAN);

	public SingleForce yellowOnRed = new SingleForce(25. / 50, 50, 1, -1, Color.RED);
	public SingleForce yellowOnGreen = new SingleForce(50. / 51, 51, 2, 0, Color.GREEN);
	public SingleForce yellowOnYellow = new SingleForce(50. / 70, 70, 2, 2, Color.YELLOW);
	public SingleForce yellowOnCyan = new SingleForce(25. / 50, 50, 1, -1, Color.CYAN);

	public SingleForce cyanOnRed = new SingleForce(25. / 50, 50, 1, -1, Color.RED);
	public SingleForce cyanOnGreen = new SingleForce(50. / 51, 51, 2, 0, Color.GREEN);
	public SingleForce cyanOnYellow = new SingleForce(50. / 70, 70, 2, 2, Color.YELLOW);
	public SingleForce cyanOnCyan = new SingleForce(25. / 50, 50, 1, -1, Color.CYAN);

	public final SingleForce[][] forceMatrix = { { redOnRed, redOnRed, redOnRed, redOnRed },
			{ greenOnRed, greenOnGreen, greenOnYellow, greenOnCyan },
			{ yellowOnRed, yellowOnGreen, yellowOnYellow, yellowOnCyan },
			{ cyanOnRed, cyanOnGreen, cyanOnYellow, cyanOnCyan } };

	// DRAW TOGGLES
	public boolean drawFPS = true;
	public boolean drawForces = true;
	public boolean drawCamera = false;
	public boolean drawEnvBorder = false;
	public boolean drawSHGrid = false;

}
