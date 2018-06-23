package bots;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import de._3m5.gameoflifearena.bots.Bot;
import de._3m5.gameoflifearena.game.Board;
import de._3m5.gameoflifearena.game.Cell;

class Cells extends ArrayList<Cell> {
	Cells(Cells cells) {
		super(cells);
	}
	Cells() {
		super(128);
	}
	Cells(int n) {
		super(n);
	}
	Cells(Collection<Cell> cells){
		super(cells);
	}
	Cells(Cell[] cells) {
		super(cells.length);
		for (Cell cell : cells) add(cell);
	}
}

public class Bot2 implements Bot {

	@Override
	public String getName() {
		return "PayloadBot";
	}
	
	Random ran = new Random();
	
	@Override
	public Collection<Point> getMoves(Board board, int ownTeam) {
		try {
			
			/* block */
			int x_offset = ran.nextInt(board.width());
			int y_offset = ran.nextInt(board.height());
			for (int iy = 0; iy < board.height(); iy++) {
				for (int ix = 0; ix < board.width(); ix++) {
					int x = (ix + x_offset) % board.width();
					int y = (iy + y_offset) % board.height();
					Cells block = doBlock(x, y, board, ownTeam);
					if (block != null) {
						Collection<Point> points = cellsToPoints(block);
						
						if (!addKillerPoint(board, points, ownTeam)) {
							addRandomPoint(board, points);
						}
						
						
						return points;
					}
				}
			}
			
			
			ArrayList<Point> moves = new ArrayList<Point>(5);
			return moves;
		} catch(Exception e) {
			return new ArrayList<Point>();
		}
	}

	private void addRandomPoint(Board board, Collection<Point> points) {
		int x = ran.nextInt(board.width());
		int y = ran.nextInt(board.height());
		points.add(new Point(x, y));
	}
	
	private boolean addKillerPoint(Board board, Collection<Point> points, int ownTeam) {
		int x_offset = ran.nextInt(board.width());
		int y_offset = ran.nextInt(board.height());
		for (int iy = 0; iy < board.height(); iy++) {
			for (int ix = 0; ix < board.width(); ix++) {
				int x = (ix + x_offset) % board.width();
				int y = (iy + y_offset) % board.height();
				Cell cell = board.cellAt(x, y);
				if (cell.owningTeam() != ownTeam
						&& cell.cellWillLive()
						&& cell.dominantTeam() != ownTeam
						&& cell.neighbourCount() == 3) {
					int ex, ey;
					Cell maybe;
					
					ex = cell.x() + 1;
					ey = cell.y() + 1;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
					
					ex = cell.x() + 1;
					ey = cell.y() + -1;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
					
					ex = cell.x() + 1;
					ey = cell.y() + 0;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
					
					ex = cell.x() - 1;
					ey = cell.y() + 1;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
					
					ex = cell.x() - 1;
					ey = cell.y() + -1;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
					
					ex = cell.x() - 1;
					ey = cell.y() + 0;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
					
					ex = cell.x() - 0;
					ey = cell.y() + 1;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
					
					ex = cell.x() - 0;
					ey = cell.y() - 1;
					maybe = board.cellAt(ex, ey);
					if (maybe != null && !maybe.isAlive()) {
						points.add(maybe.position());
						return true;
					}
				}
			}
		}
		return false;
	}

	private Collection<Point> cellsToPoints(Cells cells) {
		ArrayList<Point> points = new ArrayList<Point>(5);
		for (Cell cell : cells) {
			points.add(cell.position());
		}
		return points;
	}
	
	private Cells doBlock(int x, int y, Board board, int ownTeam) {
		Cells block = new Cells(new Cell[]{
				board.cellAt(x, y),
				board.cellAt(x, y+1),
				board.cellAt(x+1, y),
				board.cellAt(x+1, y+1)
			});
		boolean all_empty = true;
		boolean all_no_neighbors = true;
		for (Cell cell : block) {
			if (cell == null) {
				return null;
			}
			all_empty &= !cell.isAlive();
			all_no_neighbors &= cell.neighbourCount() == 0;
		}
		if (all_empty && all_no_neighbors) {
			return block;
		}
		return null;
	}
}