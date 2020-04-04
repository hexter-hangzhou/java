package classic;

import java.util.List;
import java.util.ArrayList;

public class State {

	private Board board;
	private int currPlayer;

	public State() {
	}

	public State(int[][] s, int player) {
		board = new Board(s);
		currPlayer = player;
	}

	@Override
	public State clone() {
		State another = new State();
		another.board = this.board.clone();
		another.currPlayer = this.currPlayer;
		return another;
	}

	public boolean execMove(int xx, int yy) {
		if (board.isLegalMove(xx, yy, currPlayer)) {
			board.execMove(xx, yy, currPlayer);
		} else {
			return false;
		}
		currPlayer = -currPlayer;
		return true;
	}

	public void printInfo() {
		board.printBoard();
		System.out.print("Black: " + board.getScore(Player.BLACK) + "  ");
		System.out.println("White: " + board.getScore(Player.WHITE));
		System.out.println("Current player: " + currPlayer);
		System.out.println(possibleMoves());
	}

	public List<int[]> possibleMoves() {
		List<int[]> moves = new ArrayList<int[]>();
		for (int i = 0; i < Board.rowNum; ++i) {
			for (int j = 0; j < Board.columnNum; ++j) {
				if (board.isLegalMove(i, j, currPlayer)) {
					int[] buf = { i, j };
					moves.add(buf);
				}
			}
		}
		return moves;
	}

	public Board getBoard() {
		return board;
	}

	public int currWinner() {
		if (board.getScore(Player.WHITE) > board.getScore(Player.BLACK)) {
			return Player.WHITE;
		} else if (board.getScore(Player.WHITE) == board.getScore(Player.BLACK)) {
			return 0;
		} else {
			return Player.BLACK;
		}
	}

	public int getCurrPlayer() {
		return currPlayer;
	}

	// bigger value, better for currPlayer
	public int evaluate() {
		int mobility = possibleMoves().size();
		int enemy = currPlayer == Player.BLACK ? Player.WHITE : Player.BLACK;
		int stability = board.getStability(enemy) - board.getStability(currPlayer);
		return stability * 10 - mobility;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		State state = (State) o;
		return board.equals(state.board) && currPlayer == (state.currPlayer);
	}

	public boolean gameOver() {
		return possibleMoves().size() == 0; // board.isFull();
	}

	public void randomMove() {
		List<int[]> moves = possibleMoves();
		int[] buf = moves.get((int) (Math.random() * moves.size()));
		execMove(buf[0], buf[1]);
	}
}
