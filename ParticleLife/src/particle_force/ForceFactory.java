package particle_force;

import java.util.ArrayList;
import java.util.List;

import main.Config;

public class ForceFactory {
	private static ForceFactory instance = null;

	private ForceFactory() {
	}

	public static ForceFactory getInstance() {
		if (instance == null)
			instance = new ForceFactory();

		return instance;
	}
	
	public List<MultiForce> createMultiForceListFromForceMatrix(SingleForce[][] matrix){
		/*
		 *  	  A  B
		 * A aa ba 
		 * B ab bb
		 * xy = x -> b
		 * 
		 * */
		
		List<MultiForce> forces = new ArrayList<>();
		for(int row = 0; row < matrix.length; row++) {
			MultiForce force = new MultiForce();
			for(int col = 0; col < matrix[0].length; col++) {
				force.addForce(matrix[row][col]);
			}
			forces.add(force);
		}
		return forces;
	}

}
