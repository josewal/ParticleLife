package particle;

import java.awt.Color;

import vector.Vector2D;

public class ParticleForce {
	public double rMin;
	public double rMid;
	public double rMax;
	public double midCharge;
	public double repulsionCharge;

	public Color midColor = new Color(255, 0, 0, 50);

	public ParticleForce(double rMin, double rMax, double repulsionCharge, double midCharge) {
		super();
		if (rMin >= rMax) {
			throw new Error("rMin >= rMax");
		}

		this.rMin = rMin;
		this.rMid = (rMin + rMax) / 2;
		this.rMax = rMax;
		this.midCharge = midCharge;
		this.repulsionCharge = repulsionCharge;
	}

	public double getStrength(double dist) {
		if (dist < rMin) {
			return -(dist - rMin) / (rMin - 0) * repulsionCharge;
		}
		if (dist < rMid) {
			return (dist - rMin) / (rMid - rMin) * midCharge;
		}
		if (dist < rMax) {
			return -(dist - rMax) / (rMax - rMid) * midCharge;
		}
		return 0;
	}

	public Vector2D calculateForceDirection(Vector2D relativePosition, Vector2D relativeVel) {
//		TODO: instatiates a lot of vectors
		Vector2D direction;

		if (relativePosition.getLengthSq() != 0) {
			direction = relativePosition.getNormalized();
			return direction;
		}

		if (relativeVel.getLengthSq() != 0) {
			direction = relativeVel.getNormalized();
			return direction;
		}

//		direction = Vector2D.randomInRange(-1, 1, -1, 1);
		direction = new Vector2D(1,0);
		direction.normalize();
		return direction;

	}

	public Vector2D getForceVector(Vector2D relativePosition, Vector2D relativeVel) {
		Vector2D direction = calculateForceDirection(relativePosition, relativeVel);
		double dist = relativePosition.getLength();
		double strength = getStrength(dist);
		direction.multiply(strength);
		return direction;
	}
}
