package main;

import java.awt.Color;

import particle.ParticleForce;

public class Config {
	private static Config instance = null;

	private Config() {
	}

	public static Config getInstance() {
		if (instance == null)
			instance = new Config();

		return instance;
	}

	// SCREEN SIZE
	public final int screenWidth = 800;
	public final int screenHeight = 800;

	// SIM PARAMS
	public final double simStep = 0.1;

	// FPS
	public final int FPS = 60;
	public final int UPF = 1; // updates per frame

	// ENV PARAMS
	public final double envWidth = 600;
	public final double envHeight = 600;
	public final boolean envWrapped = true;

	public final int start_number_of_particles = 500;
	public final double airResistanceCoef = 1;
	
	//SHGRID PARAMS
	public final double cellSize = 50;
	public final int cols = (int) (envWidth / cellSize);
	public final int rows = (int) (envHeight / cellSize);

	// PARTICLE PARAMS
	public final Color defColor = Color.WHITE;

	// CAMERA
	public final int startWidth = 600;
	public final int startHeight = 600;

	// PARTICLE SET
	public final int[] amounts = { 100, 1000, 100 };
	public final ParticleForce[] forces = { 
			new ParticleForce(25, 50, 10, -1), 
			new ParticleForce(25, 50, 10, 1),
			new ParticleForce(25, 100, 10, -2) };
	public final Color[] colors = { 
			Color.RED, 
			Color.GREEN,
			Color.BLUE };
}
