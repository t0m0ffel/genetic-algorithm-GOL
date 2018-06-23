

import de._3m5.gameoflifearena.game.Board;

import java.awt.*;
import java.util.Collection;

public interface Bot {

    public Collection<Point> getMoves(Board board, int ownTeam);

    public String getName();

}
