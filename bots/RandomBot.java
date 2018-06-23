package bots;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import de._3m5.gameoflifearena.bots.Bot;
import de._3m5.gameoflifearena.game.Board;

public class RandomBot implements Bot {

	private Random ran = new Random();
	
	@Override
	public String getName() {
		return "randy random";
	}

	@Override
	public Collection<Point> getMoves(Board board, int ownTeam) {



		ArrayList<Point> ans = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			int x = ran.nextInt(board.width());
			int y = ran.nextInt(board.height());
			ans.add(new Point(x, y));
		}
		return ans;
	}
	
}
