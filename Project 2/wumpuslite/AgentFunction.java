/*
 * Class that defines the agent function.
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 * 
 * Last modified 2/19/07 
 * 
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 * 
 */

import java.util.Random;

class AgentFunction {
	
	// string to store the agent's name
	// do not remove this variable
	private String agentName = "Binah";

	private boolean bump;
	private boolean glitter;
	private boolean breeze;
	private boolean stench;
	private boolean scream;
	
	
	
	// code from Binah
	
	// This represents the known world as a 3D array of strings.
	// Specifically, the first 2 dimensions will indicate location, while the 3rd dimension will be used to
	// seperately store where we believe the Wumpus and the Pits are.
	// see the constructor for more
	private String[][][] currentWorld;
	
	// a pair of integers to denote our current location.
	private int xLoc;
	private int yLoc;
	
	// To keep track of which way we are facing, we will use an enum.
	private enum Direction {
		NORTH,
		EAST,
		SOUTH,
		WEST
	}
	
	private Direction myDirection;
	
	// and a boolean to track if we've shot our arrow or not.
	boolean hasArrow;
	// one more to track if the wumpus is alive.
	boolean wumpusAlive;
	
	// finally an integer to keep track of number of pits located. This is useful I promise.
	int pitsFound;

	public AgentFunction()
	{
		
		// now that this class has been constructed, we begin giving values to our variables.
		
		// We start by initializing every entry in the top layer of "currentWorld" 3D string array as "?"
		// as this represents having no information.
		this.currentWorld = new String[4][4][3];
		for (int i=0; i<4; i++) {
			for (int j=0; j<4; j++) {
				// note the [0] at the end, as currentWorld[x][y][1] is used to store information on pits,
				// and currentWorld[x][y][2] is used to store information on the Wumpus.
				this.currentWorld[i][j][0]="?";
			}
		}
		
		// The location of our agent is represented by the pair of ints "xLoc" and "yLoc". 
		// note to please run the "WorldApplication" main function with "randomAgentLoc" set to false
		// either by running it with the arg '-a false' on the command line or 
		// by editting the code itself, so that the agent always spawns at location (0,0).
		this.xLoc=0;
		this.yLoc=0;
		
		// Also we are always initialized facing East, so we set our direction to East.
		this.myDirection = Direction.EAST;
		
		// and we always start with an arrow.
		this.hasArrow=true;
		// and the wumpus is presumably always going to start alive.
		this.wumpusAlive=true;
		// and we don't start knowing where pits are.
		this.pitsFound=0;
		
		// Now that the agent has been initialized, all knowledge must be gained in what we learn through percepts.
	}

	public int process(TransferPercept tp)
	{	
		// read in the current percepts
		bump = tp.getBump();
		glitter = tp.getGlitter();
		breeze = tp.getBreeze();
		stench = tp.getStench();
		scream = tp.getScream();
		

		// Continued code from Binah.
		
		
		// I'm going to divide this up into 2 sections, information gathering VIA percepts, and decision making through
		// the updated model.
		
		// Section 1 is updating the data, so here we go.
		
		// Step 1:
		// If the current location is glittering, we know the gold is here. So we grab it, and win.
		// forget updating the current world to look pretty, instead, just win.
		if (glitter) {
			return Action.GRAB;
		}
		
		
		// Step 2: 
		// If we've heard a scream, we update that the wumpus is dead.
		if (scream) {
			// this updates the var to ensure it's dead dead.
			wumpusAlive=false;
			
			// Also, we use what direction we're facing to find the offset as to where, precisely, the Wumpus was,
			// relative to us, at least.
			int xOffset=0;
			int yOffset=0;
			switch(myDirection) {
			case NORTH:
				// against all my mathematical and computer science instincts, facing north means a positive X coord,
				// rather than a positive y coord.
				xOffset=1;
				break;
			case EAST:
				yOffset=1;
				break;
			case SOUTH:
				xOffset=-1;
				break;
			case WEST:
				yOffset=-1;
				break;
			}
			// Now we remove the Wumpus from our map by setting all entries of currentWorld[x][y][2] to empty.
			for (int i=0; i<4; i++) {
				for (int j=0; j<4; j++) {
					this.currentWorld[i][j][2]="";
				}
			}
			
			// and we also note that a wumpus is dead by 
			this.currentWorld[xLoc+xOffset][yLoc+yOffset][2]="X";
			// we also do the same for replacing the "W" character with "X", as it's possible we 100% knew that a wumpus was there.
			
			// We also need to clear up the case wherein it wasn't certain that the wumpus was in that position.
			// As such, we clear any other 
		}
		// If no such easy solution exists, time for a simple bit of information gathering and updating.
		
		// To reduce irrelevant calcuations we check to make sure our current space is a yet unexplored one.
		// To do this, we check to see if the current location, on 3rd dimention layer 0, is equal to "?".
		else if (currentWorld[xLoc][yLoc][0].equalsIgnoreCase("?")) {
			// If this is an unexplored tile, we need to gather information through our percepts.
			// To start, we check for the two most important percepts:
			
		}
		
		
		String relevantPercepts="";
		
		// if we feel a breeze, we append "B" to our relevantPercepts string.
		if (breeze) {
			relevantPercepts+="B";
		}
		
		if (stench) {
			
		}
		

		// If we bumped into a wall, we turn left. Left was chosen due to the fact that the 
		// agent starts facing east, and if they walk forward 4 times without pause 
		// (which is unlikely but not impossible), then turning right would cause another 
		// bump, another turn, then another backtrack towards (1,1)
		if (bump) {
			return Action.TURN_LEFT;
		}

		// Finally, the last point: our cowardice.

		// If we sense danger (either by feeling a breeze or smelling a stench), simply give up.
		// This ensures we will never die.
		if (breeze == true || stench == true) {
			return Action.NO_OP;
		}
		
		// If none of the above are true, we're safe to move forward.
	    return Action.GO_FORWARD;
	}
	
	// public method to return the agent's name
	// do not remove this method
	public String getAgentName() {
		return agentName;
	}
}