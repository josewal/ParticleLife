package enviroment;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import camera.Camera;
import hashgrid.SpatialHashGrid;
import main.Config;
import particle.Particle;
import particle.ParticleFactory;
import task.interactionCalculationTask;
import vector.Vector2D;

public class Enviroment {
	Config conf = Config.getInstance();
	
	ExecutorService service = Executors.newFixedThreadPool(conf.numberOfThreads);
	List<interactionCalculationTask> tasks = new ArrayList<interactionCalculationTask>();
	
	
	public ArrayList<Particle> particles;
	public SpatialHashGrid<Particle> shGrid;

	private ArrayList<Vector2D> airResistanceVectors;
	private ConcurrentHashMap<Particle, Vector2D> particleNetInteractions;

	public Enviroment() {
		airResistanceVectors = new ArrayList<>();
		particleNetInteractions = new ConcurrentHashMap<>();

		particles = new ArrayList<>();

		setupSHGrid();
		setupParticles();
	}

	public void setupSHGrid() {

		shGrid = new SpatialHashGrid<Particle>(conf.cols, conf.rows, conf.cellSize);
	}

	public void setupParticles() {
		ParticleFactory pFact = ParticleFactory.getInstance();
		loadParticleList(pFact.createParticleListFromMatrix(conf.startAmounts, conf.forceMatrix));
	}

	public void loadParticleList(List<Particle> particles) {
		for (Particle particle : particles) {
			addParticle(particle);
		}
	}

	public void addParticle(Particle particle) {
		particles.add(particle);
		shGrid.add(particle);
		airResistanceVectors.add(new Vector2D());
	}

	public Particle getParticle(int idx) {
		return particles.get(idx);
	}

	private void calculateAirResistanceVectors() {
		for (int i = 0; i < airResistanceVectors.size(); i++) {
			airResistanceVectors.get(i).set(getParticle(i).getVel());
			airResistanceVectors.get(i).multiply(airResistanceVectors.get(i).getLength());
			airResistanceVectors.get(i).multiply(-conf.airResistanceCoef);
		}
	}

	private void applyAirResistanceVectors() {
		for (int i = 0; i < airResistanceVectors.size(); i++) {
			getParticle(i).applyForce(airResistanceVectors.get(i));
		}
	}

	private void applyParticleInteractionsVectors() {
		for (HashMap.Entry<Particle, Vector2D> interaction : particleNetInteractions.entrySet()) {
			Particle particle = interaction.getKey();
			Vector2D force = interaction.getValue();
			particle.applyForce(force);
		}
	}

	public Vector2D calculateToroidalRelativePos(Vector2D to, Vector2D from) {
		double dX = (to.x - from.x);
		double dY = (to.y - from.y);

		double absdX = Math.abs(dX);
		double absdY = Math.abs(dY);

		double toroidalDx = dX;
		double toroidalDy = dY;

		if (absdX > conf.envWidth / 2) {
			toroidalDx = -Math.signum(dX) * (conf.envWidth - absdX);
		}

		if (absdY > conf.envHeight / 2) {
			toroidalDy = -Math.signum(dY) * (conf.envHeight - absdY);
		}

		return new Vector2D(toroidalDx, toroidalDy);
	}

	public Vector2D calculateInteraction(Particle acting, Particle on) {
		Vector2D relativePos = calculateToroidalRelativePos(on.getPos(), acting.getPos());

		Vector2D relativeVel = Vector2D.subtract(on.getVel(), acting.getVel());
		Vector2D force = acting.force.getForceVector(relativePos, relativeVel, on.color);
		return force;
	}

	public void zeroOutNetInteractionsMap() {
		particleNetInteractions.clear();
	}

	/**
     * Iterates over env.particles [acter], gathers all particles that are acted on [actingOn] and calls calculateInteraction(acter, actingOn) a if this vector is nonzero than puts it in the particleIneractions 
     * @return null
     */
	public void executeInteractionCalculationTasks() {
		zeroOutNetInteractionsMap();
//		service.invokeAll(tasks);
//		for (interactionCalculationTask interactionCalculationTask : tasks) {
//			service.execute(interactionCalculationTask);
//		}
		try {
			service.invokeAll(tasks);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	public void acterActs(Particle acter, Set<Particle> actingOnParticles) {
		for (Particle actingOn : actingOnParticles) {
			if (acter == actingOn) {
				continue;
			}
			
			Vector2D interaction = calculateInteraction(acter, actingOn);
			if (interaction.getLengthSq() == 0) {
				continue;
			}
			
			addSingleInteractionToNetInteractions(actingOn, interaction);
		}
	}

	public Set<Particle> gatherParticlesInInteractionRadius(Particle acter) {
		double forceRadius = acter.force.maxRadius;
		double queryX = acter.getX() - forceRadius;
		double queryY = acter.getY() - forceRadius;

		Set<Particle> actingOnParticles = shGrid.elementsInRectQuery(queryX, queryY, 2 * forceRadius,
				2 * forceRadius);
		return actingOnParticles;
	}

	public void addSingleInteractionToNetInteractions(Particle actingOn, Vector2D interaction) {
		Vector2D netInteraction = particleNetInteractions.get(actingOn);
		if (netInteraction == null) {
			particleNetInteractions.put(actingOn, interaction);
		} else {
			netInteraction.add(interaction);
		}
	}

	public void update(double dt) {
		calculateAirResistanceVectors();
		applyAirResistanceVectors();
		
		createInteractionCalculationTasks();
		executeInteractionCalculationTasks();
		applyParticleInteractionsVectors();

		updateParticles(dt);

		boundAllParticles();

		shGrid.update();
	}

	private void createInteractionCalculationTasks() {
		tasks.clear();
//		System.out.println(Runtime.getRuntime().availableProcessors());		
		for(int i = 0; i < particles.size(); i += conf.taskSize) {
			interactionCalculationTask task = new interactionCalculationTask(this, i, Math.min(i + conf.taskSize, particles.size()));
			tasks.add(task);
		}
	}

	public void pushParticlesToCenter() {
		for (Particle particle : particles) {
			Vector2D push = particle.getPos().getSubtracted(new Vector2D(conf.envWidth / 2, conf.envHeight / 2));
			push.normalize();
			push.multiply(-0.001);
			particle.applyForce(push);
		}
	}

	public void updateParticles(double dt) {
		for (Particle particle : shGrid.getElements()) {
			particle.update(dt);
		}
	}

	public void boundAllParticles() {
		for (Particle particle : shGrid.getElements()) {
			boundParticle(particle);
		}
	}

	public void boundParticle(Particle particle) {
		double x = particle.getX();
		double y = particle.getY();

		if (x < 0) {
			particle.setX(conf.envWidth);
		} else if (x > conf.envWidth) {
			particle.setX(0);
		}

		if (y < 0) {
			particle.setY(conf.envHeight);
		} else if (y > conf.envHeight) {
			particle.setY(0);
		}
	}

	public void draw(Graphics2D g2, Camera c) {
		if (conf.drawSHGrid) {
			shGrid.draw(g2, c);
		}
		if (conf.drawEnvBorder) {
			drawWorldBorder(g2, c);
		}
		if (conf.drawForces) {
			for (Particle particle : particles) {
				particle.drawForce(g2, c);
			}
		}
		
		Set<Particle> nbs = gatherParticlesInInteractionRadius(particles.get(0));
		for (Particle particle : nbs) {
			particle.highlight(g2, c, new Color(255,0,255,100));
		}

//		int[][] inView = shGrid.bucketIdxInRectQuery(c.worldX, c.worldY, c.width*c.zoom, c.height*c.zoom);
//		for (int[] idx : inView) {
//			Set<Particle> particles = shGrid.getElementsFromBucket(idx[0], idx[1]);
		for (Particle particle : particles) {
			particle.draw(g2, c);
		}
//		}
	}

	public void drawWorldBorder(Graphics2D g2, Camera c) {
		g2.setColor(Color.YELLOW);
		g2.drawRect(c.getFrameX(0), c.getFrameY(0), c.zoomElongation(conf.envWidth), c.zoomElongation(conf.envHeight));
	}

	public void drawVector(Graphics2D g2, Camera c, Vector2D toDraw, Vector2D where) {
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		Vector2D end = Vector2D.add(toDraw, where);
		g2.drawLine(c.getFrameX(where.x - c.worldCenterX), c.getFrameY(where.y - c.worldCenterY),
				c.zoomElongation(end.x - c.worldCenterX), c.zoomElongation(end.y - c.worldCenterY));
	}

}
