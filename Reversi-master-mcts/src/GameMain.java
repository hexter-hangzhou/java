import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import classic.PkApp;
import layout.TableLayout;

public class GameMain extends JFrame {

	static int playerturn; // 1表示黑方执棋，-1表示白方执棋,这里假设电脑是白方，人是黑方
	static boolean isMachine;
	static int blockwidth;
	static int blockheight;
	static int blackplayer = Constants.BLACK;
	static int whiteplayer = Constants.WHITE;

	private static GameBoard gameboard = new GameBoard();
	private static PkApp pkapp = new PkApp();;
	static TextArea tf = new TextArea(20, 7);
	private Thread thread = null;
	private State state;
	private Mcts mcts = new Mcts();
	private int round=0;
	public GameMain() {
		// TODO Auto-generated constructor stub
		tf.setFont(new Font("黑体",Font.BOLD,16));
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
				
			
		
				
				if(thread!=null)return;
				
					 thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                            	round++;
                            	System.out.println("round: " + round+";ok="+pkapp.checkstate(state.getState()));
                            	int[] buf=pkapp.getAction();
                				if(buf==null)break;
                				int x= buf[1]+1;
                				int y=buf[0]+1;
                                int action = 8 * (x - 1) + y - 1;
                				
                                pkapp.gogogo(buf);
                                
                                gameboard.setValueByindex(x - 1, y - 1, playerturn);
            					
                                
            					
            					
            					state.changeState(action);
            					setBoard(state);
            					gameboard.setNewchessBoard(x - 1, y - 1);
                                gameboard.repaint();
                                state.changePlayer();
                                playerturn = -playerturn;
                                
                                gameboard.calWBnums();
                            	tf.setText("U are Black:\nBlack:"+gameboard.blackNums+"\nWhtie:"+gameboard.whiteNums);
                                if(state.isTerminal()) {
                                    System.out.println("Winner: " + state.getWinner());
                                    return;
                                }
                                if(state.getActionSet().size() == 0) {
                                    state.changePlayer();
                                    playerturn = -playerturn;
                                    if(state.getActionSet().size() == 0) {
                                        System.out.println("Winner: " + state.getWinner());
                                    }
                                    break;
                                }
                                else {
                                    state = mcts.UctSearch(state);
                                    setBoard(state);
									//gameboard.setValueByindex(state.getLeadAction() / 8, state.getLeadAction() % 8, playerturn);
                                   buf[0]= (state.getLeadAction() % 8);
                                   buf[1]=state.getLeadAction() / 8;
                                    pkapp.gogogo(buf);
                                    gameboard.setNewchessBoard(state.getLeadAction() / 8, state.getLeadAction() % 8);
                                    gameboard.repaint();
                                    playerturn = -playerturn;
                                    if(state.getActionSet().size() == 0) {
                                        playerturn = -playerturn;
                                        state.changePlayer();
                                        continue;
                                    }
                                    
                                }
                            }
                            gameboard.calWBnums();
                            tf.setText("U are Black:\nBlack:"+gameboard.blackNums+"\nWhtie:"+gameboard.whiteNums);
                        }
                    });
					thread.start();
					isMachine = false;
				}
				
			

			}
		);

	}

	public void initializeGame() {
		// 中间四个子分别为

		gameboard.setValueByindex(3, 3, whiteplayer);
		gameboard.setValueByindex(3, 4, blackplayer);
		gameboard.setValueByindex(4, 3, blackplayer);
		gameboard.setValueByindex(4, 4, whiteplayer);
		gameboard.setWBnums(2, 2); // 初始的时候黑方和白方均为2个
	}

	private void machineFirst(boolean flag) {
		if (flag) {
			state = new State(true, Constants.BLACK);
			state = mcts.UctSearch(state);
			setBoard(state);
			isMachine = flag;
			playerturn = blackplayer;
		} else {
			state = new State(false, Constants.BLACK);
			setBoard(state);
			isMachine = false;
			playerturn = blackplayer;
		}
	}

	public void setBoard(State state) {
		int[][] s = state.getState();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (s[i][j] == Constants.BLACK) {
					gameboard.setValueByindex(i, j, blackplayer);
				} else if (s[i][j] == Constants.WHITE) {
					gameboard.setValueByindex(i, j, whiteplayer);
				}
			}
		}
		gameboard.repaint();
	}

	public static void main(String[] args) {

		double size[][] = { { 0.25, TableLayout.FILL }, { TableLayout.FILL } };

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
		newWbgoGame.machineFirst(false);
		tf.setText("U are Black:\nBlack:" + gameboard.blackNums + "\nWhtie:" + gameboard.whiteNums);

	}

}