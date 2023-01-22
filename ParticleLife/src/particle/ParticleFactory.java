package particle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import main.Config;
import particle_force.ForceFactory;
import particle_force.MultiForce;
import particle_force.SingleForce;
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
	
	public Particle createParticle(Vector2D pos, Vector2D vel, MultiForce force, Color color) {
		Particle particle = new Particle(force, color);
		particle.setPos(pos);
		particle.setVel(vel);
		return particle;
	}
	
	public Particle createRandomWorldPosRandomVelParticle(MultiForce force, Color color) {
		Vector2D randomPos = Vector2D.randomInRange(0, conf.envWidth, 0, conf.envWidth);
		Vector2D randomVel = Vector2D.randomInRange(-5, 5, -5, 5);
		return createParticle(randomPos, randomVel, force, color);
	}
	
	public List<Particle> createParticleListOfOneType(int amount, MultiForce force, Color color){
		ArrayList<Particle> particles = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			particles.add(createRandomWorldPosRandomVelParticle(force, color));
		}
		return particles;
	}
	
	public List<Particle> createParticleListFromArrays(int[] amounts, MultiForce[] forces, Color[] colors){
		ArrayList<Particle> particles = new ArrayList<>();
		for (int i = 0; i < amounts.length; i++) {
			particles.addAll(createParticleListOfOneType(amounts[i], forces[i], colors[i]));
		}
		return particles;
	}
	
	public List<Particle> createParticleListFromMatrix(int[] amounts, SingleForce[][] matrix){		
		ForceFactory fFact = ForceFactory.getInstance();
		List<MultiForce> forcesList= fFact.createMultiForceListFromForceMatrix(conf.forceMatrix);
		
		MultiForce[] forces = new MultiForce[amounts.length];
		Color[] colors = new Color[amounts.length];
		
		for (int col = 0; col < matrix.length; col++) {
			forces[col] = forcesList.get(col);
			colors[col] = matrix[col][col].color;
		}
		
		return createParticleListFromArrays(amounts, forces, colors);
	}
	
}
