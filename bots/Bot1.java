package bots;

import java.awt.Point;
import java.util.HashSet;

import de._3m5.gameoflifearena.bots.Bot;
import de._3m5.gameoflifearena.game.Board;


public class Bot1 implements Bot{
private int count = -1;
	
	@Override
	public HashSet<Point> getMoves(Board board, int ownTeam) {
	    count++;
		int middleHeight = board.height()/2;
	    int middleWidth = board.width()/2;
	    Point pentimoCenter =  calculateCenter(middleHeight, middleWidth, count);
	    HashSet<Point> moves = createRPentimo(pentimoCenter);
	    return moves;
	}

	private HashSet<Point> createRPentimo(Point center){
		HashSet<Point> moves = new HashSet<Point>();
	    moves.add(center);
	    moves.add(new Point(center.x-1,center.y));
	    moves.add(new Point(center.x+1,center.y));
	    moves.add(new Point(center.x+1,center.y+1));
	    moves.add(new Point(center.x,center.y-1));
	    return moves;
	}
	
	private Point calculateCenter(int y, int x, int move){
		int offsetFactor;
		if(move%2 == 0){
			offsetFactor = 1;
		}else{
			offsetFactor = -1;
		}
		move = move%5;
		int offset = offsetFactor*move*2; 		
		if(x<y){
			return new Point(x+offsetFactor,y+offset);
		}
		return new Point(x+offset,y+offsetFactor);
	}
	
	
	@Override
	public String getName() {
		return "PentominoBot";
	}

}
