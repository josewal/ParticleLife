package particle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import main.Config;
import vector.Vector2D;

public class ParticleFactory {
	private static ParticleFactory instance = null;
	final private Config conf = Config.getInstance();

	private ParticleFactory() {
	}

	public static ParticleFactory getInstance() {
		if (instance == null)
			instance = new ParticleFactory();

		return instance;
	}
	
	public Particle createParticle(Vector2D pos, Vector2D vel, ParticleForce force, Color color) {
		Particle particle = new Particle(force, color);
		particle.setPos(pos);
		particle.setVel(vel);
		return particle;
	}
	
	public Particle createRandomWorldPosRandomVelParticle(ParticleForce force, Color color) {
		Vector2D randomPos = Vector2D.randomInRange(0, conf.envWidth, 0, conf.envWidth);
		Vector2D randomVel = Vector2D.randomInRange(-5, 5, -5, 5);
		return createParticle(randomPos, randomVel, force, color);
	}
	
	public List<Particle> createParticleListOfOneType(int amount, ParticleForce force, Color color){
		ArrayList<Particle> particles = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			particles.add(createRandomWorldPosRandomVelParticle(force, color));
		}
		return particles;
	}
	
	public List<Particle> createParticleList(int[] amounts, ParticleForce[] forces, Color[] colors){
		ArrayList<Particle> particles = new ArrayList<>();
		for (int i = 0; i < amounts.length; i++) {
			particles.addAll(createParticleListOfOneType(amounts[i], forces[i], colors[i]));
		}
		return particles;
	}
	
}
