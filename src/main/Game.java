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
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300, HEIGHT = 300;
	private int PLAYER = 1, OPPONENT = -1, CURRENT = PLAYER;

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

		}

		// resetBoard();
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

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent arg0) {

	}

	public void keyTyped(KeyEvent arg0) {

	}

	private void resetBoard() {
		for (int i = 0; i < BOARD.length; i++) {
			for (int j = 0; j < BOARD.length; j++) {
				BOARD[i][j] = 0;
			}
		}
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
