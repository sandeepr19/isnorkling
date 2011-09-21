package isnork.g9.comm;

import isnork.sim.GameObject;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class IncomingMessageQueue {
	
	private PriorityQueue<Message> msgHeap = new PriorityQueue<Message>();
	private static final Direction[] choices = new Direction[] {Direction.E, Direction.NE,
		Direction.N, Direction.NW, Direction.W, Direction.SW, Direction.S, Direction.SE,
		Direction.STAYPUT}; 
	
	
	public Suggestion getHVTDirection(Point2D myPosition){
		
		if(msgHeap.isEmpty()){
			System.out.println("Heap is empty");
			return new Suggestion(GameObject.Direction.STAYPUT,1);
		}
		
		Message msg = msgHeap.remove();
		Point2D dest = msg.getSenderLocation();
		System.out.println("Comm Dest: "+dest);
		System.out.println("Received Estimated value: "+ msg.getEstimatedValue());
		//Simulator id screwed up. The Y axis is flipped
		double thetaRad = Math.atan2(myPosition.getY()-dest.getY(), dest.getX()-myPosition.getX());
		double thetaDeg = thetaRad * 180 / Math.PI;
		if(thetaDeg < 0 ) thetaDeg += 360;
		int dirChoice = ((int)thetaDeg)/45 + ( ((int) thetaDeg)%45 < 23 ? 0 : 1);
		System.out.println("My location: "+ myPosition);
		System.out.println("Comm Direction : "+choices[dirChoice]);
		return new Suggestion(choices[dirChoice],msg.getEstimatedValue());
		
	}
	
	public void load(Point2D myPosition, Set<Observation> whatYouSee, Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations, Encoding encoding){
		
		tick();
		
		for(iSnorkMessage iMsg : incomingMessages){
			Message msg = encoding.decode(iMsg);
			MessageEvaluator<Message> evaluator = new MessageEvaluatorImpl<Message>();
			double val = evaluator.evaluate(myPosition, msg);
			msg.setEstimatedValue(val);
			if(val > 0 && val < 1){
				System.out.println("Loading Estimate: "+msg.getEstimatedValue());
				msgHeap.add(msg);
			}
			
		}
		
	}
	
	private void tick(){
		
		List<Message> deadMessages = new ArrayList<Message>();
		
		for(Message msg : msgHeap){
			msg.age();
			if(msg.die())deadMessages.add(msg);
		}
		
		for(Message msg : deadMessages)
			msgHeap.remove(msg);
	}

}
