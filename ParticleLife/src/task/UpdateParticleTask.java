package task;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import enviroment.Enviroment;
import particle.Particle;

public class UpdateParticleTask extends RecursiveAction{
	Enviroment env;
	int from;
	int to;
	
	double dt;
	
	

	public UpdateParticleTask(Enviroment env, int from, int to, double dt) {
		super();
		this.env = env;
		this.from = from;
		this.to = to;
		this.dt = dt;
	}



	@Override
	protected void compute() {
		if(to-from < 10) {
			for (int i = from; i < to; i++) {
				Particle p = env.particles.get(i);
				p.update(dt);
				env.boundParticle(p);
				
				
			}
		}else {
			int mid =(to+from)/2;
			UpdateParticleTask t1 = new UpdateParticleTask(env, from, mid+1, dt);
			UpdateParticleTask t2= new UpdateParticleTask(env, mid+1, to, dt);
			invokeAll(t1, t2);
		}
		
	}

}
