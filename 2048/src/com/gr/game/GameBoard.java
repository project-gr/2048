package com.gr.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GameBoard {
	
	public static final int ROWS = 4; // number of tile in row
	public static final int COLS = 4; // number of tile in col
	
	private final int staringTiles = 2; // number of tile at beginning (less than 16)
	
	private Tile[][] board; // store tiles are on the board in 2d array 
	private boolean dead; // lose
	private boolean won; // win the game
	private BufferedImage gameBoard; // background of game board
	private BufferedImage finalBoard; // game board and all the tiles 
	private int x; // where to render on the screen
	private int y;
	
	private static int SPACING = 10; // Space between tile (pixel)
	public static int BOARD_WIDTH = (COLS + 1)* SPACING + COLS * Tile.WIDTH; // get the actual width in pixels of the board
	public static int BOARD_HEIGHT = (ROWS + 1)* SPACING + ROWS * Tile.HEIGHT;
	
	private boolean hasStarted;
	
	public GameBoard(int x, int y) {
		this.x = x; // where we draw this in the screen
		this.y = y;
		board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		createBoardImage();
		start();
	}
	
	private void createBoardImage() {
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setColor(Color.darkGray);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
		g.setColor(Color.lightGray);
		
		// draw tile to game board
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int x = SPACING + SPACING *col + Tile.WIDTH * col; // set x position
				int y = SPACING + SPACING *row + Tile.HEIGHT *row; // set y position
				g.fillRoundRect(x, y, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
			}
		}
	}
	
	private void start () { // spawn in the first 2 tiles
		for (int i = 0; i < staringTiles; i++) {
			spawnRandom();
		}
	}
	
	private void spawnRandom() { // pick a spot & decide 2 or 4 is spawn
		Random random = new Random();
		boolean notValid = true;
		
		while (notValid) {
			int location = random.nextInt(ROWS * COLS);
			int row = location / ROWS;
			int col = location % COLS;		
			Tile current = board[row][col]; // current value position
			
			if (current == null) { // empty spot 
				int value = random.nextInt(10) < 9 ? 2 : 4;
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
		}
	}
	
	public int getTileX (int col) {
		return SPACING + col * Tile.WIDTH + col *SPACING;
	}
	
	public int getTileY (int row) {
		return SPACING + row * Tile.WIDTH + row *SPACING;
	}
	
	public void render (Graphics2D g) {
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics(); // draw to final board
		g2d.drawImage(gameBoard, 0, 0, null);
		
		// draw tiles
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if (current == null) continue;
				current.render(g2d);
			}
		}
		
		g.drawImage(finalBoard, x, y, null);
		g2d.dispose();
	}
	
	public void update() {
		checkKeys();
		
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = board[row][col];
				if (current == null) continue;
				current.update();
				// reset position
				if (current.getValue() == 2048) { // if there is 2048 -> won 
					won = true;
				}
			}
		}
	}
	
	private void checkKeys() { // moving tiles
		if (Keyboard.typed(KeyEvent.VK_LEFT)) {
			// move tiles left
			if (!hasStarted) hasStarted = true;
		}
		if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
			// move tiles right
			if (!hasStarted) hasStarted = true;
		}
		if (Keyboard.typed(KeyEvent.VK_UP)) {
			// move tiles up
			if (!hasStarted) hasStarted = true;
		}
		if (Keyboard.typed(KeyEvent.VK_DOWN)) {
			// move tiles down
			if (!hasStarted) hasStarted = true;
		}
	}
	
}