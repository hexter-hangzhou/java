package classic;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MCTS {
	
    private static int[][] corner = {
            {0, 0},
            {0, 7},
            {7, 0},
            {7, 7}
    };


    public int[] nextMove(State state) {
        List<int[]> moves = state.possibleMoves();
        for (int[] move : corner) {
            if (moves.contains(move)) {
                return move;
            }
        }
        Node root = new Node(state);
        return root.nextMove();
    }

    
}
