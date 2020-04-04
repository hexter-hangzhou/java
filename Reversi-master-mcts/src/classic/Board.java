package classic;

public class Board {
	static final int[][] DFB = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, Player.WHITE, Player.BLACK, 0, 0, 0 }, { 0, 0, 0, Player.BLACK, Player.WHITE, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, };
	static final int rowNum = 8;
	static final int columnNum = 8;
	private int[][] map = new int[rowNum][columnNum];
	private static int[][] d1 = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 1, -1 } };
	private static int[][] d2 = { { -1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 1 } };

	Board() {
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < columnNum; j++) {
				map[i][j] = 0;
			}
		}
	}

	Board(int[][] s) {
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < columnNum; j++) {
				map[i][j] = s[i][j];
			}
		}
	}

	public int[][] getMap() {
		return map;
	}

	int getScore(int player) {
		int count = 0;
		for (int[] mapRow : map) {
			for (int buf : mapRow) {
				if (buf == player) {
					count++;
				}
			}
		}
		return count;
	}

	boolean isLegalMove(int xx, int yy, int player) {
		// used in possibleMoves(), must in board
		if (map[xx][yy] != 0) {
			return false;
		}
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				if (i == j && i == 0) {
					continue;
				}
				int x = xx;
				int y = yy;
				do {
					x += i;
					y += j;
				} while (inBoard(x, y) && (map[x][y] == -player));
				if (inBoard(x, y) && map[x][y] == (player)) {
					if (x - xx != i || y - yy != j) {
						return true;
					}
				}
			}
		}
		return false;
	}

	static boolean inBoard(int x, int y) {
		return 0 <= x && x < Board.rowNum && 0 <= y && y < Board.columnNum;
	}

	void execMove(int xx, int yy, int player) {
		if (!inBoard(xx, yy)) {
			return;
		}
		map[xx][yy] = player;
		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				if (i == j && i == 0) {
					continue;
				}
				int x = xx;
				int y = yy;
				do {
					x += i;
					y += j;
				} while (inBoard(x, y) && map[x][y] == (-player));
				if (inBoard(x, y) && map[x][y] == (player)) {
					x -= i;
					y -= j;
					while (inBoard(x, y) && map[x][y] == (-player)) {
						map[x][y] = player;
						x -= i;
						y -= j;
					}
				}
			}
		}
	}

	int getStability(int player) {
		int sum = 0;
		for (int i = 0; i < rowNum; ++i) {
			for (int j = 0; j < columnNum; ++j) {
				if (map[i][j] == player) {
					sum += singleStability(i, j);
				}
			}
		}
		return sum;
	}

	private int singleStability(int xx, int yy) {
		int player = map[xx][yy];
		int count = 0;
		for (int index = 0; index < 4; ++index) {
			int enemyNum = 0;
			for (int i = 0; i < 2; ++i) {
				int[][] dd = i == 0 ? d1 : d2;
				int x = xx;
				int y = yy;
				while (inBoard(x, y) && map[x][y] == player) {
					x += dd[index][0];
					y += dd[index][1];
				}
				if (!inBoard(x, y)) {
					count++;
					break;
				} else if (map[x][y] != 0) {
					enemyNum++;
					if (enemyNum == 2) {
						count++;
					}
				}
			}
		}
		return count;
	}

	void printBoard() {
		System.out.println("-------------");

		for (int[] mapRow : map) {
			for (int tempPlayer : mapRow) {
				System.out.print(tempPlayer);
			}
			System.out.println();
		}
	}

	@Override
	protected Board clone() {
		Board another = new Board();
		for (int i = 0; i < rowNum; ++i) {
			System.arraycopy(this.map[i], 0, another.map[i], 0, columnNum);
		}
		return another;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Board board = (Board) o;
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < columnNum; j++) {
				if (map[i][j] != board.map[i][j]) {
					return false;
				}
			}
		}
		return true;
	}

	boolean isFull() {
		for (int i = 0; i < rowNum; ++i) {
			for (int j = 0; j < columnNum; ++j) {
				if (map[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int[][] d3 = { { 1, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 1, -1, -1, -1, -1, -1, -1, -1 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };
		Board board = new Board(d3);
		System.out.println(board.getStability(Player.BLACK));
	}
}
