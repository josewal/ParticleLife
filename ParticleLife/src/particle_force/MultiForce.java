package particle_force;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import vector.Vector2D;

public class MultiForce {
	Map<Color, SingleForce> colorToForceMap = new HashMap<>();
	public double maxRadius = 0;
	public double avgMinRadius = 0;

	public MultiForce() {
		// TODO Auto-generated constructor stub
	}

	public void addForce(SingleForce force) {
		colorToForceMap.put(force.color, force);
		maxRadius = Math.max(maxRadius, force.maxRadius);
		avgMinRadius = ((colorToForceMap.size() - 1) * avgMinRadius + force.minRadius) / colorToForceMap.size();
	}

	public double getForceMaxRadius(Color clr) {
		return colorToForceMap.get(clr).maxRadius;
	}

	public Vector2D getForceVector(Vector2D relativePos, Vector2D relativeVel, Color clr) {
		SingleForce force = colorToForceMap.get(clr);
		if(force == null) {
			return new Vector2D();
		}
		return force.getForceVector(relativePos, relativeVel);
	}
}
