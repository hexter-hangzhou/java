package classic;

public class PkApp {

	private State state;
	private MCTS2 mcts = new MCTS2();

	public PkApp() {
		state = new State(Board.DFB, Player.BLACK);
	}

	public int[] getAction() {

		if (state.getCurrPlayer() == Player.BLACK) {
			if (state.gameOver()) {
				System.out.println("Winner: " + state.currWinner());

			} else {
				int[] buf = mcts.nextMove(state);
				
				return buf;
			}
		}
		return null; 
	
	}
	public void gogogo(int[] buf) {
		state.execMove(buf[0], buf[1]);
	}
	public boolean checkstate(int[][] s) {
		int[][]buf=state.getBoard().getMap();
		 for (int i = 0; i < Board.rowNum; ++i) {
	            for (int j = 0; j < Board.columnNum; ++j) {
	            	if(s[i][j]!=buf[j][i]) return false;
	            }
	            }
		 return true;
	}
}
