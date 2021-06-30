package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300, HEIGHT = 300;
	private int PLAYER = 1, OPPONENT = -1, CURRENT = OPPONENT;
	private boolean reset = false;

	private BufferedImage PLAYER_SPRITE, OPPONENT_SPRITE;
	private int[][] BOARD = new int[3][3];
	private boolean pressed = false;
	private int mx, my;

	public Game() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addKeyListener(this);
		this.addMouseListener(this);
		try {
			PLAYER_SPRITE = ImageIO.read(getClass().getResource("/player.png"));
			OPPONENT_SPRITE = ImageIO.read(getClass().getResource("/opponent.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		turn();
		matchResult();
		resetBoard();
	}

	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		for (int i = 0; i < BOARD.length; i++) {
			for (int j = 0; j < BOARD.length; j++) {
				g.setColor(Color.black);
				g.drawRect(i * 100, j * 100, 100, 100);
				if (BOARD[i][j] == PLAYER) {
					g.drawImage(PLAYER_SPRITE, i * 100 + 25, j * 100 + 25, 50, 50, null);
				} else if (BOARD[i][j] == OPPONENT) {
					g.drawImage(OPPONENT_SPRITE, i * 100 + 25, j * 100 + 25, 50, 50, null);
				}
			}
		}

		g.dispose();
		bs.show();
	}

	public static void main(String args[]) {
		Game game = new Game();
		JFrame frame = new JFrame("Tic Tac Toe");
		frame.add(game);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		new Thread(game).start();
	}

	public void run() {

		while (true) {
			tick();
			render();
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private void turn() {
		if (CURRENT == PLAYER) {
			if (pressed) {
				pressed = false;
				mx /= 100;
				my /= 100;
				if (BOARD[mx][my] == 0) {
					BOARD[mx][my] = PLAYER;
					CURRENT = OPPONENT;
				}
			}
		} else if (CURRENT == OPPONENT) {
			minMax();
		}
	}

	private void minMax() {
		for (int xx = 0; xx < BOARD.length; xx++) {
			for (int yy = 0; yy < BOARD.length; yy++) {
				if (BOARD[xx][yy] == 0) {
					Node bestMove = getBestMove(xx, yy, 0, OPPONENT);

					BOARD[bestMove.x][bestMove.y] = OPPONENT;

					CURRENT = PLAYER;
					return;
				}
			}
		}
	}

	public Node getBestMove(int x, int y, int depth, int turn) {
		if (checkStatus() == PLAYER) {
			return new Node(x, y, depth - 10, depth);
		} else if (checkStatus() == OPPONENT) {
			return new Node(x, y, 10 - depth, depth);
		} else if (checkStatus() == 0) {
			return new Node(x, y, 0, depth);
		}

		List<Node> nodes = new ArrayList<Node>();

		for (int xx = 0; xx < BOARD.length; xx++) {
			for (int yy = 0; yy < BOARD.length; yy++) {

				if (BOARD[xx][yy] == 0) {
					Node node;
					if (turn == PLAYER) {
						BOARD[xx][yy] = PLAYER;
						node = getBestMove(xx, yy, depth + 1, OPPONENT);
						BOARD[xx][yy] = 0;
					} else {
						BOARD[xx][yy] = OPPONENT;
						node = getBestMove(xx, yy, depth + 1, PLAYER);
						BOARD[xx][yy] = 0;
					}
					nodes.add(node);
				}
			}
		}

		Node finalNode = nodes.get(0);
		for (int i = 0; i < nodes.size(); i++) {
			Node n = nodes.get(i);
			if (turn == PLAYER) {
				if (n.score > finalNode.score) {
					finalNode = n;
				}
			} else {
				if (n.score < finalNode.score) {
					finalNode = n;
				}
			}
		}

		return finalNode;

	}

	private void resetBoard() {
		if (reset) {
			reset = false;
			pressed = false;
			for (int i = 0; i < BOARD.length; i++) {
				for (int j = 0; j < BOARD.length; j++) {
					BOARD[i][j] = 0;
				}
			}
		}
	}

	private int checkStatus() {
		int primDiagPlayer = 0, primDiagEnemy = 0, secDiagPlayer = 0, secDiagEnemy = 0, checkTie = 0;
		for (int i = 0; i < BOARD.length; i++) {
			// HORIZONTAL CHECK
			if (BOARD[i][0] == PLAYER && BOARD[i][1] == PLAYER && BOARD[i][2] == PLAYER) {
				return PLAYER;
			} else if (BOARD[i][0] == OPPONENT && BOARD[i][1] == OPPONENT && BOARD[i][2] == OPPONENT) {
				return OPPONENT;
			}

			// PRIMARY DIAGONAL CHECK
			if (BOARD[i][i] == PLAYER) {
				primDiagPlayer++;
			} else if (BOARD[i][i] == OPPONENT) {
				primDiagEnemy++;
			}

			if (primDiagPlayer == 3) {
				return PLAYER;
			}

			if (primDiagEnemy == 3) {
				return OPPONENT;
			}

			for (int j = 0; j < BOARD.length; j++) {
				// VERTICAL CHECK
				if (BOARD[0][j] == PLAYER && BOARD[1][j] == PLAYER && BOARD[2][j] == PLAYER) {
					return PLAYER;
				} else if (BOARD[0][j] == OPPONENT && BOARD[1][j] == OPPONENT && BOARD[2][j] == OPPONENT) {
					return OPPONENT;
				}

				// SECONDARY DIAGONAL CHECK
				if ((i + j) == (BOARD.length - 1)) {
					if (BOARD[i][j] == PLAYER) {
						secDiagPlayer++;
					} else if (BOARD[i][j] == OPPONENT) {
						secDiagEnemy++;
					}

					if (secDiagPlayer == 3) {
						return PLAYER;
					}

					if (secDiagEnemy == 3) {
						return OPPONENT;
					}
				}

				// TIE CHECK
				if (BOARD[i][j] != 0)
					checkTie++;
			}

			if (checkTie == BOARD.length * BOARD[0].length)
				return 0;
		}
		return -10;
	}

	private void matchResult() {
		if (checkStatus() == PLAYER) {
			System.out.println("PLAYER IS THE WINNER");
		} else if (checkStatus() == OPPONENT) {
			System.out.println("YOUR OPPONENT IS THE WINNER");
		} else if (checkStatus() == 0) {
			System.out.println("TIE");
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R) {
			reset = true;
		}
	}

	public void keyReleased(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}
