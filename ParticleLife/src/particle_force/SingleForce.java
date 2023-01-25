package particle_force;

import java.awt.Color;

import vector.Vector2D;

public class SingleForce {
	public double maxRadius;
	
	public double beta;
	
	public double chargeAtMidDist;
	public double chargeAtZeroDist;

	public Color color;

	public SingleForce(double beta, double rMax, double repulsionCharge, double midCharge, Color color) {
		super();
		if (beta >= 1 || beta <= 0) {
			throw new Error("beta: " + beta + "not from (0,1)");
		}

		this.beta = beta;
		this.maxRadius = rMax;
		this.chargeAtMidDist = midCharge;
		this.chargeAtZeroDist = repulsionCharge;
		this.color = color;
	}

	public double getStrength(double dist) {	
		dist /= maxRadius;
		if(dist < this.beta) {
			return -chargeAtZeroDist * (dist/beta - 1);
		}
		
		if( dist < 1) {
			return chargeAtMidDist * (1 - Math.abs(2*dist - 1 - beta)/(1-beta));
		}
		
		return 0;
	}


	public Vector2D calculateForceNotNormilizedDirection(Vector2D relativePosition, Vector2D relativeVel) {
		Vector2D direction;

		if (relativePosition.getLengthSq() != 0) {
			return relativePosition;
		}

		if (relativeVel.getLengthSq() != 0) {
			return relativeVel;
		}

		direction = Vector2D.randomInRange(-1, 1, -1, 1);
//		direction = new Vector2D(1,0);
		return direction;

	}

	public Vector2D getForceVector(Vector2D relativePosition, Vector2D relativeVel) {
		Vector2D direction = calculateForceNotNormilizedDirection(relativePosition, relativeVel);
		double dist = relativePosition.getLength();
		
		double strength = getStrength(dist);
		direction.multiply(1/dist);
		direction.multiply(strength);
		direction.multiply(maxRadius);

		return direction;
	}
}
