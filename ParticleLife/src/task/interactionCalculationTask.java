package task;

import java.util.Set;

import enviroment.Enviroment;
import particle.Particle;

public class interactionCalculationTask implements Runnable {
	private Enviroment env;
	private int from;
	private int to;

	public interactionCalculationTask(Enviroment env, int from, int to) {
		super();
		this.env = env;
		this.from = from;
		this.to = to;
	}

	public void run() {
		calculateInteractions();
	}

	private void calculateInteractions() {
		for (int i = from; i < to; i++) {
			Particle acter = env.particles.get(i);
			Set<Particle> inInteractionRadius = env.gatherParticlesInInteractionRadius(acter);

			env.acterActs(acter, inInteractionRadius);
		}
	}
}
