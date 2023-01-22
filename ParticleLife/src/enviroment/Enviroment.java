package enviroment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import camera.Camera;
import hashgrid.SpatialHashGrid;
import main.Config;
import particle.Particle;
import particle.ParticleFactory;
import vector.Vector2D;

public class Enviroment {
	Config conf = Config.getInstance();

	private ArrayList<Particle> particles;
	public SpatialHashGrid<Particle> shGrid;

	private ArrayList<Vector2D> airResistanceVectors;
	private ArrayList<Vector2D> particleInteractions;
	

	public Enviroment() {		
		airResistanceVectors = new ArrayList<>();
		particleInteractions = new ArrayList<>();
		particles = new ArrayList<>();


		setupSHGrid();
		setupParticles();
	}

	public void setupSHGrid() {
		
		shGrid = new SpatialHashGrid<Particle>(conf.cols, conf.rows, conf.cellSize);
	}

	public void setupParticles() {
		ParticleFactory pFact = ParticleFactory.getInstance();
		loadParticleList(pFact.createParticleListFromMatrix(conf.amounts, conf.forceMatrix));
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
		particleInteractions.add(new Vector2D());
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
		for (int i = 0; i < particleInteractions.size(); i++) {
			getParticle(i).applyForce(particleInteractions.get(i));
		}
	}

	public Vector2D calculateInteraction(Particle acting, Particle on) {
		Vector2D relativePos = Vector2D.subtract(on.getPos(), acting.getPos());
		Vector2D relativeVel = Vector2D.subtract(on.getVel(), acting.getVel());
		Vector2D force = acting.force.getForceVector(relativePos, relativeVel, on.color);
		return force;
	}
	
	public void zeroOutInteractions() {
		for (int i = 0; i < particleInteractions.size(); i++) {
			Vector2D vector2d = particleInteractions.get(i);
			vector2d.set(0,0);
		}
	}

	public void calculateAllInteractions() {
		zeroOutInteractions();
		
		for (int i = 0; i < particles.size(); i++) {
			Particle acter = particles.get(i);

			double forceRadius = acter.force.maxRadius;
			double queryX = acter.getX() - forceRadius;
			double queryY = acter.getY() - forceRadius;

			Set<Particle> actingOnParticles = shGrid.elementsInRectQuery(queryX, queryY, 2*forceRadius,
					2*forceRadius);

			for (Particle actingOn : actingOnParticles) {
				if(acter != actingOn) {
					Vector2D interaction = calculateInteraction(acter, actingOn);
					actingOn.applyForce(interaction);
				}
			}
		}
	}

	public void update(double dt) {
		calculateAirResistanceVectors();
		applyAirResistanceVectors();
		
		calculateAllInteractions();
		applyParticleInteractionsVectors();

		updateParticles(dt);

		boundParticles();

		shGrid.update();
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

	public void boundParticles() {
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
		shGrid.draw(g2, c);
	
		int[][] inView = shGrid.bucketIdxInRectQuery(c.worldX, c.worldY, c.width, c.height);
		for (int[] idx : inView) {
			Set<Particle> particles = shGrid.getElementsFromBucket(idx[0], idx[1]);
			for (Particle particle : particles) {
				particle.draw(g2, c);
			}
		}
	}

	public void drawWorldBorder(Graphics2D g2, Camera c) {
		g2.setColor(Color.YELLOW);
		g2.drawRect(0 - (int) c.worldX, 0 - (int) c.worldY, (int) conf.envWidth, (int) conf.envHeight);
	}

}
