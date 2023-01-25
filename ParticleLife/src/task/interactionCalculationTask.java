package task;

import java.util.Set;
import java.util.concurrent.RecursiveAction;

import enviroment.Enviroment;
import particle.Particle;

public class interactionCalculationTask extends RecursiveAction {
	private static final long serialVersionUID = 1L;
	private Enviroment env;
	private int taskSize;
	private int from;
	private int to;

	public interactionCalculationTask(Enviroment env, int from, int to, int taskSize) {
		super();
		this.env = env;
		this.from = from;
		this.to = to;
		this.taskSize = taskSize;
	}

	private void calculateInteractions() {
		for (int i = from; i < to; i++) {
			env.calculateAirResistanceVector(i);
			Particle acter;
			try {
				acter = env.particles.get(i);
				Set<Set<Particle>> interactionBuckets = env.gatherParticlesInInteractionRadius(acter);
				for (Set<Particle> bucket : interactionBuckets) {
					env.acterActs(acter, bucket);

				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	


	@Override
	protected void compute() {
		if(to - from > taskSize) {
			int mid = (from + to)/2;
			interactionCalculationTask t1 = new interactionCalculationTask(env, from, mid + 1, taskSize);
			interactionCalculationTask t2 = new interactionCalculationTask(env, mid + 1, to, taskSize);
			invokeAll(t1, t2);
		}else {
			calculateInteractions();
		}
		
	}
}
