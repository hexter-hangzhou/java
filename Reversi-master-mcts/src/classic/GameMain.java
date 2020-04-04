package classic;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Random;

import javax.swing.JFrame;
import layout.TableLayout;

public class GameMain extends JFrame {
	static int playerturn; // 1表示黑方执棋，-1表示白方执棋,这里假设电脑是白方，人是黑方
	static boolean isMachine;
	static int blockwidth;
	static int blockheight;
	static int blackplayer = Player.BLACK;
	static int whiteplayer = Player.WHITE;

	private static GameBoard gameboard = new GameBoard();
	static TextArea tf = new TextArea(20, 7);

	private State state;
	private MCTS mcts = new MCTS();

	public GameMain() {
		// TODO Auto-generated constructor stub
		tf.setFont(new Font("黑体", Font.BOLD, 16));
		gameboard.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (state.getCurrPlayer() != Player.BLACK) {
					return;
				}
				int xmouse = e.getX();
				int ymouse = e.getY();
				blockwidth = gameboard.getEachWidth();
				blockheight = gameboard.getEachHeight();
				int x = xmouse / blockwidth; // 这里的x是二维数组中的行，换言之，即是鼠标的纵坐标相对开头的偏移单位数
				int y = ymouse / blockheight;

				x -= 1;
				y -= 1;
				System.out.println("x: " + x + ";y: " + y);
				if (!state.execMove(x, y)) {
					return;
				}

				{

					gameboard.setNewchessBoard(x, y);
					gameboard.repaint();
					playerturn = state.getCurrPlayer();
					gameboard.calWBnums();
					tf.setText("U are Black:\nBlack:" + gameboard.blackNums + "\nWhtie:" + gameboard.whiteNums);
					if (state.gameOver()) {
						System.out.println("Winner: " + state.currWinner());
						tf.setText("U are Black:\nBlack:" + gameboard.blackNums + "\nWhtie:" + gameboard.whiteNums
								+ "\nWinner: " + state.currWinner());
						return;
					}

					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							// while (true)
							{
								if (state.gameOver()) {
									System.out.println("Winner: " + state.currWinner());
									tf.setText("U are Black:\nBlack:" + gameboard.blackNums + "\nWhtie:"
											+ gameboard.whiteNums + "\nWinner: " + state.currWinner());

								} else {
									int[] buf = mcts.nextMove(state);
									state.execMove(buf[0], buf[1]);
									
									gameboard.setNewchessBoard(buf[0], buf[1]);
									gameboard.repaint();
									playerturn = state.getCurrPlayer();
									
								}
							}
							gameboard.calWBnums();
							tf.setText("U are Black:\nBlack:" + gameboard.blackNums + "\nWhtie:" + gameboard.whiteNums);
						}
					});
					thread.start();
					isMachine = false;
				}

			}
		});
	}

	public void initializeGame() {
		// 中间四个子分别为
		machineFirst(false);
		gameboard.calWBnums();
		tf.setText("U are Black:\nBlack:" + gameboard.blackNums + "\nWhtie:" + gameboard.whiteNums);
	}

	private void machineFirst(boolean flag) {
		if (flag) {
			state = new State(Board.DFB, Player.BLACK);
			
			setBoard(state);
			isMachine = flag;
			playerturn = blackplayer;
		} else {
			state = new State(Board.DFB, Player.BLACK);
			setBoard(state);
			isMachine = false;
			playerturn = blackplayer;
		}
	}

	public void setBoard(State state) {

		gameboard.setBoard(state.getBoard());
		gameboard.repaint();
	}

	public static void main(String[] args) {

		double size[][] = { { 0.25, TableLayout.FILL }, { TableLayout.FILL } };
		Random r1 = new Random();
		
		TableLayout mtTableLayout = new TableLayout(size);
		GameMain newWbgoGame = new GameMain();

		newWbgoGame.setBounds(200, 200, 514, 410);
		newWbgoGame.setResizable(true);
		newWbgoGame.setLayout(mtTableLayout);
		newWbgoGame.add(gameboard, "1,0");
		newWbgoGame.add(tf, "0.25");
		newWbgoGame.setResizable(false);
		newWbgoGame.setTitle("Reversi");
		newWbgoGame.setVisible(true);
		newWbgoGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newWbgoGame.initializeGame();

	}
}
