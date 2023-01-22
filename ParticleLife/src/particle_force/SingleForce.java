package particle_force;

import java.awt.Color;

import vector.Vector2D;

public class SingleForce {
	public double minRadius;
	public double midRadius;
	public double maxRadius;
	
	public double chargeAtMidDist;
	public double chargeAtZeroDist;

	public Color color;

	public SingleForce(double rMin, double rMax, double repulsionCharge, double midCharge, Color color) {
		super();
		if (rMin >= rMax) {
			throw new Error("rMin >= rMax");
		}

		this.minRadius = rMin;
		this.midRadius = (rMin + rMax) / 2;
		this.maxRadius = rMax;
		this.chargeAtMidDist = midCharge;
		this.chargeAtZeroDist = repulsionCharge;
		this.color = color;
	}

	public double getStrength(double dist) {
		if (dist < minRadius) {
			return -(dist - minRadius) / (minRadius - 0) * chargeAtZeroDist;
		}
		if (dist < midRadius) {
			return (dist - minRadius) / (midRadius - minRadius) * chargeAtMidDist;
		}
		if (dist < maxRadius) {
			return -(dist - maxRadius) / (maxRadius - midRadius) * chargeAtMidDist;
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
