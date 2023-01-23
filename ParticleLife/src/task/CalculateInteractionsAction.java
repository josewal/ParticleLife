package task;

import java.util.Set;
import java.util.concurrent.RecursiveAction;

import enviroment.Enviroment;
import particle.Particle;
import vector.Vector2D;

public class CalculateInteractionsAction extends RecursiveAction {
	private static final long serialVersionUID = 1L;

	private Enviroment env;
	private int from;
	private int to;

	public CalculateInteractionsAction(Enviroment env, int from, int to) {
		super();
		this.env = env;
		this.from = from;
		this.to = to;
	}

	@Override
	protected void compute() {
		if (to - from < 10) {
			calculateInteractions();
		} else {
			int middle = (to - from / 2);
//			System.out.printf("Task: Pending tasks: %s\n", getQueuedTaskCount());
			CalculateInteractionsAction t1 = new CalculateInteractionsAction(env, from, middle + 1);
			CalculateInteractionsAction t2 = new CalculateInteractionsAction(env, middle + 1, to);
			invokeAll(t1, t2);
		}
	}

	private void calculateInteractions() {
		for (int i = from; i < to; i++) {
			Particle acter = env.particles.get(i);

			double forceRadius = acter.force.maxRadius;
			double queryX = acter.getX() - forceRadius;
			double queryY = acter.getY() - forceRadius;

			Set<Particle> actingOnParticles = env.shGrid.elementsInRectQuery(queryX, queryY, 2 * forceRadius,
					2 * forceRadius);

			for (Particle actingOn : actingOnParticles) {
				if (acter != actingOn) {
					Vector2D interaction = env.calculateInteraction(acter, actingOn);
					if (interaction.getLengthSq() != 0) {
						Vector2D netInteraction = env.particleInteractions.get(actingOn);
						if (netInteraction == null) {
							env.particleInteractions.put(actingOn, interaction);
						} else {
							netInteraction.add(interaction);
						}
					}
				}
			}
		}
	}
}
