
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameBoard extends JPanel {

	static int bplayer = Constants.BLACK;
	static int wplayer = Constants.WHITE;

	private Image backgroud = Toolkit.getDefaultToolkit().getImage("image/qp.png");
	private Image whiteChess = Toolkit.getDefaultToolkit().getImage("image/White.png");
	private Image newWhite = Toolkit.getDefaultToolkit().getImage("image/newWhite.png");
	private Image newBlack = Toolkit.getDefaultToolkit().getImage("image/newBlack.png");
	private Image blank = Toolkit.getDefaultToolkit().getImage("image/blank.png");
	private Image blackChess = Toolkit.getDefaultToolkit().getImage("image/Black.png");
	int blackNums, whiteNums;
	int imgWidth, imgHeight;
	int chessWidth, chessHeight;
	int winWidth, winHeight;
	int eachWidth;
	int eachHeight;

	private int nx = -1, ny = -1;

	private int[][] chessBoard = new int[8][8];

	public void setWBnums(int b, int w) {
		this.blackNums = b;
		this.whiteNums = w;
	}

	public void calWBnums() {
		int w = 0, b = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessBoard[i][j] == bplayer)
					b += 1;
				if (chessBoard[i][j] == wplayer)
					w += 1;
			}
		}
		setWBnums(b, w);
	}

	public void setNewchessBoard(int x, int y) {
		this.nx = x;
		this.ny = y;
	}

	public int getEachWidth() {
		return eachWidth;
	}

	public int getEachHeight() {
		return eachHeight;
	}

	public GameBoard() {
		// TODO Auto-generated constructor stub

		imgWidth = backgroud.getWidth(this);
		imgHeight = backgroud.getHeight(this);
		chessWidth = blank.getWidth(this);
		chessHeight = blank.getHeight(this);
		winWidth = this.getWidth();
		winHeight = this.getHeight();
		eachWidth = winWidth / 10;
		eachHeight = winHeight / 10;
		// System.out.println(eachWidth+" "+eachHeight);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				chessBoard[i][j] = 0;
			}
		}

	}

	@Override
	public void repaint() {
		// TODO Auto-generated method stub
		super.repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		// System.out.println("paint happens");
		winWidth = this.getWidth();
		winHeight = this.getHeight();
		imgWidth = backgroud.getWidth(this);
		imgHeight = backgroud.getHeight(this);
		chessWidth = blank.getWidth(this);
		chessHeight = blank.getHeight(this);
		eachWidth = winWidth / 10;
		eachHeight = winHeight / 10;

		g.drawImage(backgroud, 0, 0, winWidth, winHeight, 0, 0, imgWidth, imgHeight, null);
		// g.drawImage(blank, 0, 0, winWidth, winHeight, 0, 0,chessWidth, chessHeight,
		// null);
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (chessBoard[i][j] == 0) {
					g.drawImage(blank, eachWidth * (j + 1), eachHeight * (i + 1), eachWidth * (j + 2),
							eachHeight * (i + 2), 0, 0, chessWidth, chessHeight, this);
				} else if (chessBoard[i][j] == bplayer) {
					if (i != nx || j != ny)
						g.drawImage(blackChess, eachWidth * (j + 1), eachHeight * (i + 1), eachWidth * (j + 2),
								eachHeight * (i + 2), 0, 0, chessWidth, chessHeight, this);
					else {

						g.drawImage(newBlack, eachWidth * (j + 1), eachHeight * (i + 1), eachWidth * (j + 2),
								eachHeight * (i + 2), 0, 0, chessWidth, chessHeight, this);

					}
				} else if (chessBoard[i][j] == wplayer) {
					if (i != nx || j != ny)
						g.drawImage(whiteChess, eachWidth * (j + 1), eachHeight * (i + 1), eachWidth * (j + 2),
								eachHeight * (i + 2), 0, 0, chessWidth, chessHeight, this);
					else
						g.drawImage(newWhite, eachWidth * (j + 1), eachHeight * (i + 1), eachWidth * (j + 2),
								eachHeight * (i + 2), 0, 0, chessWidth, chessHeight, this);
				}
			}
		}

	}

	public void newChessComes(int x, int y, int player) {

		if (x < 0 || x > 7 || y < 0 || y > 7)
			return;
		chessBoard[x][y] = player;
	}

	public void setValueByindex(int x, int y, int player) {
		// TODO Auto-generated method stub
		if (x < 0 || x > 7 || y < 0 || y > 7) {
			return;
		}
		chessBoard[x][y] = player;
		nx = x;
		ny = y;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);

	}

}
