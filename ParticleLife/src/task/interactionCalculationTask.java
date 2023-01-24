package task;

import java.util.Set;
import java.util.concurrent.Callable;

import enviroment.Enviroment;
import particle.Particle;

public class interactionCalculationTask implements Callable<Boolean> {
	private Enviroment env;
	private int from;
	private int to;

	public interactionCalculationTask(Enviroment env, int from, int to) {
		super();
		this.env = env;
		this.from = from;
		this.to = to;
	}

	private void calculateInteractions() {
		for (int i = from; i < to; i++) {
			Particle acter;
			try {
				acter = env.particles.get(i);
				Set<Particle> inInteractionRadius = env.gatherParticlesInInteractionRadius(acter);

				env.acterActs(acter, inInteractionRadius);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public Boolean call() throws Exception {
		calculateInteractions();
		return true;
	}
}
