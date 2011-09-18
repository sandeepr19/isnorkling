/**
 * Really Dumb Player to demonstrate interacting with simulator API
 * @author Group 10: Yufei, Chris, Neil, Sandeep
 */

package isnork.g9;

import isnork.g9.comm.CommPrototype;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Set;

public class ReallyDumbPlayer extends Player {

	private CommPrototype commPrototype;
	
	private Direction[] choices = new Direction[] { Direction.N, Direction.NW, 
			Direction.W, Direction.SW, Direction.S, 
            Direction.SE, Direction.E, Direction.NE, 
            Direction.STAYPUT }; 
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Real Dumb Player by Group 10";
	}

	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n) {
		// TODO Auto-generated method stub

	}

	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		// TODO Auto-generated method stub
		
		// Assume that we have some idea of the kind and distribution of sea creatures
		// Hence, we shall communicate approximately how much happiness & in what direction
		return commPrototype.createMessage(myPosition, whatYouSee, incomingMessages, playerLocations);
		
	}

	/**
	 * pick one out of the nine choices with equal probability
	 */
	@Override
	public Direction getMove() {
		//Equal probability for each of the 8 compass direction, as well as staying stationary
		return choices[this.random.nextInt(9)];
	}
}