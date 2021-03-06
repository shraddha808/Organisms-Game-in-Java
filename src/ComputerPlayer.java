import java.util.Random;

/**
 * This class is an implementation of the Player interface. It is a computer
 * player, that had a brain of its own which decides the movements for each
 * round
 * 
 * @author ShadyJ
 *
 */
public class ComputerPlayer implements Player {
	private static final String NAME = "Computer Player";
	private int key;
	private GameConfig game;
	private int direction = Constants.STAYPUT;

	/**
	 * This method is used to register the players, along with a key
	 */
	@Override
	public void register(GameConfig game, int key) {

		this.game = game;
		this.key = key;

	}

	/**
	 * This returns the name of the player
	 */
	@Override
	public String name() {

		return NAME;
	}

	/**
	 * This method is the move method - it is called to make the player choose
	 * the next move for the round
	 * 
	 * @return - a Move object
	 */
	@Override
	public Move move(boolean[] food, int[] neighbors, int foodleft, int energyleft) {
		Move move = null;
		int childpos = Brain(food, neighbors, foodleft, energyleft);
		// if don't have energy to move or reproduce, and there is more food in
		// the cell you eat
		switch (direction) {
		case 0:
			move = new Move(Constants.STAYPUT);
			break;
		case 1:
			move = new Move(Constants.WEST);
			break;
		case 2:
			move = new Move(Constants.EAST);
			break;
		case 3:
			move = new Move(Constants.NORTH);
			break;
		case 4:
			move = new Move(Constants.SOUTH);
			break;
		case 5:
			move = new Move(direction, childpos, key);
			break;
		}
		return move;
	}

	/**
	 * This is the brain code for the computer player. It decides what move to
	 * make, depending on the information provided to it
	 * 
	 * @param food
	 *            - if there's food available on neighboring cells
	 * @param neighbors
	 *            - if the neighboring cells are occupied
	 * @param foodleft
	 *            - the amount of food on the current cell
	 * @param energyleft
	 *            - the energy left for the organism
	 * @return
	 */
	private int Brain(boolean[] food, int[] neighbors, int foodleft, int energyleft) {

		int childpos = Constants.STAYPUT;

		if (energyleft < game.v() && foodleft > 0) {
			direction = Constants.STAYPUT;
		}
		// if energy left is less than half of max, and food is left
		// eat
		else if (energyleft < game.M() / 2 && foodleft > 0) {

			direction = Constants.STAYPUT;
		}
		// if energy left is less than half, there is no food on the cell
		// and the energy left is more than that for moving
		// move to a cell with food, or select any any cell to move to

		else if (energyleft <= game.M() / 2 && energyleft > game.v() && foodleft == 0) {

			for (int i = 1; i < food.length; i++) {

				if (food[i] == true) {
					if (neighbors[i] == 0)
						direction = i;
					break;
				} else {

					for (int j = 1; j < neighbors.length; j++) {
						if (neighbors[j] == 0) {
							direction = j;
							break;
						}
						direction = Constants.STAYPUT;
					}
				}
			}
		}

		// if energy left is more than half, and there is no food on the cell
		// and the energy is more than required to reproduce
		// reproduce on a cell that has food.
		// else reproduce on any cell that is not occupied
		// if all cells are occupied, stay put.
		else if (energyleft > game.M() / 2 && foodleft == 0 && energyleft > game.v()) {

			for (int i = 1; i < neighbors.length; i++) {
				if (neighbors[i] == 0) {
					if (food[i] == true) {
						direction = Constants.REPRODUCE;
						childpos = i;
						break;
					}
					childpos = i;
				} else {
					direction = Constants.STAYPUT;
				}
			}

		} else {
			direction = Constants.STAYPUT;
			childpos = Constants.STAYPUT;
		}
		return childpos;

	}

}
