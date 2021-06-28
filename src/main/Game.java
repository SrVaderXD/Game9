package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 300, HEIGHT = 300;
	public int PLAYER = 1, OPPONENT = -1, CURRENT = PLAYER;

	public BufferedImage PLAYER_SPRITE, OPPONENT_SPRITE;
	public int[][] BOARD = new int[3][3];

	public Game() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addKeyListener(this);
		try {
			PLAYER_SPRITE = ImageIO.read(getClass().getResource("/player.png"));
			OPPONENT_SPRITE = ImageIO.read(getClass().getResource("/opponent.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void tick() {
		if (CURRENT == PLAYER) {

		} else if (CURRENT == OPPONENT) {

		}
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

		for (int xx = 0; xx < BOARD.length; xx++) {
			for (int yy = 0; yy < BOARD.length; yy++) {
				g.setColor(Color.black);
				g.drawRect(xx * 100, yy * 100, 100, 100);
				if (BOARD[xx][yy] == PLAYER) {
					g.drawImage(PLAYER_SPRITE, xx * 100, yy * 100, null);
				} else if (BOARD[xx][yy] == OPPONENT) {
					g.drawImage(OPPONENT_SPRITE, xx * 100, yy * 100, null);
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

}
