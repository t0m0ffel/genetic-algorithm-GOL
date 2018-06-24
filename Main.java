

import bots.Bot1;
import bots.Bot2;
import bots.RandomBot;
import de._3m5.gameoflifearena.SwingArenaView;
import de._3m5.gameoflifearena.bots.Bot;
import de._3m5.gameoflifearena.game.BlockBoardFactory;
import de._3m5.gameoflifearena.game.GameController;
import t.MuteBot;


public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


        Bot[] opponents = new Bot[]{
                new RandomBot(),
                new Bot1(),
                new Bot2(),
        };


        for (Bot opponent : opponents) {
            GameController gc = new GameController(
                    new BlockBoardFactory(),
                    opponent,

                    new MuteBot(100, 20, 10, 70)
            );
            gc.setDebugMode(true);
            SwingArenaView view = new SwingArenaView(gc, 10);
            view.setVisible(true);
            view.run();

        }


    }


}
