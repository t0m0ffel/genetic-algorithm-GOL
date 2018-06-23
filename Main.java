

import bots.Bot1;
import bots.Bot2;
import bots.MuteBot;
import bots.RandomBot;
import de._3m5.gameoflifearena.SwingArenaView;
import de._3m5.gameoflifearena.bots.Bot;
import de._3m5.gameoflifearena.game.BlockBoardFactory;
import de._3m5.gameoflifearena.game.GameController;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        Bot[] opponents = new Bot[]{
                new RandomBot(),
                new Bot2(),
                new Bot2(),
        };


        for (Bot opponent : opponents) {
            GameController gc = new GameController(
                    new BlockBoardFactory(),
                    new MuteBot(40, 83, 2, 70),
                    opponent
            );
            SwingArenaView view = new SwingArenaView(gc, 200);
            view.setVisible(true);
            view.run();
        }


    }


}
