package t;

import de._3m5.gameoflifearena.bots.Bot;
import de._3m5.gameoflifearena.game.Action;
import de._3m5.gameoflifearena.game.Board;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class MuteBot implements Bot {

    private int[] params;

    public MuteBot(int... params) {
        this.params = params;
        Util.round = 1;
    }

    @Override
    public String getName() {
        return "GenBot";
    }

    @Override
    public Collection<Point> getMoves(Board board, int ownTeam) {
        ownTeam--;

        Collection<Point> ans = null;
        try {
            ans = new Genetic(params).run(board, ownTeam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Util.round++;

        return ans;
    }

}

class Util {


    static int round = 1;
    private static Random random = new Random();

    static boolean random(float p) {
        return random.nextFloat() < p;
    }

    static List<Action> calcMoves(Collection<Point> points, int team) {
        return points.stream().map(p -> new Action(p, team)).collect(Collectors.toList());
    }

    static <T> List<T> randomChoice(List<T> input) {
        return randomChoice(input, input.size());
    }

    static <T> List<T> randomChoice(List<T> input, int n) {


        List<T> out = new LinkedList<>();
        input = new LinkedList<>(input);
        if (input.size() < n) {
            System.out.println("N to large");
            return input;
        }

        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            try {
                int randomIndex = rand.nextInt(input.size());
                T randomElement = input.get(randomIndex);
                out.add(randomElement);
                input.remove(randomIndex);
            } catch (Exception e) {

                System.out.println("error " + n + " length " + input.size());

            }
        }
        return out;

    }
}

class Genetic {

    private int initialPopSize;
    private int generations;
    private float mutationRate;
    private int depth;

    Genetic(int... params) {
        //initialPopSize generations depth mutationRate
        //initialPopSize generations depth
        this.initialPopSize = params[0];
        this.generations = params[1];
        this.depth = params[2];
        this.mutationRate = 1f / params[3];
    }


    private List<Point> getFreeCells(Board board) {
        List<Point> freeCells = new LinkedList<>();
        for (int i = 0; i < board.width(); i++) {
            for (int j = 0; j < board.height(); j++) {
                if (board.coordsValid(i, j) && !board.cellAt(i, j).isAlive()) {
                    freeCells.add(new Point(i, j));

                }
            }
        }
        return freeCells;
    }


    List<Point> run(Board board, int team) {

        int mostFitCount = initialPopSize / 3 + (initialPopSize / 3) % 2;
        int pointsAvailable = 5 * depth;

        List<Point> freeCells = getFreeCells(board);


        List<Chromosome> population = new LinkedList<>();
        for (int i = 0; i < initialPopSize; i++) {
            Chromosome chromosome = new Chromosome(Util.randomChoice(freeCells, pointsAvailable), board, team);
            population.add(chromosome);
        }

        int scorecount = 0;
        int bestscore = -1000;
        int i = 0;
        while (Thread.currentThread().isInterrupted() && i < generations) {
            //for (int i = 0; i < generations; i++) {
            i++;
            sortPop(population, board, team);

            population = population.subList(0, population.size() - mostFitCount);

            List<Chromosome> fittest = new LinkedList<>(population.subList(0, mostFitCount));

            for (int j = 0; j < mostFitCount; j += 2) {
                population.add(fittest.get(j).crossover(fittest.get(j + 1)));
                population.add(fittest.get(j + 1).crossover(fittest.get(j)));
            }
            population.forEach(c -> c.mutate(this.mutationRate, getFreeCells(board)));


            float lastscore = population.get(0).fitness();


            if (lastscore > bestscore) {
                System.out.println("new score  " + bestscore + " " + lastscore);
                bestscore = (int) lastscore;
                scorecount = 0;
            } else {
                scorecount++;
                if (scorecount > 5) {
                    if (depth > 1)
                        System.out.println("scorecount " + i);
                    // break;
                }
            }
        }
        sortPop(population, board, team);


        return population.get(0).getGens().subList(0, 5);

    }

    private void sortPop(List<Chromosome> population, Board board, int team) {


        Collections.sort(population);

    }


}

class Chromosome implements Comparable<Chromosome> {

    private float fitnessScore;
    private List<Point> gens;
    private Board board;
    private int team;

    Chromosome(List<Point> gens, Board board, int team) {
        this.gens = gens;
        this.board = board;
        this.team = team;
        fitnessScore = _fitness();
    }


    private float _fitness() {

        board = board.getCopy();

        List<Action> actions = Util.calcMoves(gens, team + 1);

        float totalScore = 0;
        for (int i = 0; i < actions.size(); i += 5) {
            board.calcTurn(actions.subList(i, 5 + i));

            int[] cells = board.countCells();
            totalScore += (team == 0 ? cells[0] - cells[1] : cells[1] - cells[0]) / ((i / 5) + 1);
        }

        return totalScore;
    }


    public float fitness() {
        return fitnessScore;
    }

    public Chromosome crossover(Chromosome other) {
        List<Point> newGens = new LinkedList<>(gens.subList(0, 3));
        newGens.addAll(other.getGens().subList(3, other.getGens().size()));
        return new Chromosome(newGens, this.board, this.team);
    }


    public void mutate(float mutateProb, List<Point> freeCells) {
        if (Util.random(mutateProb)) {
            Random random = new Random();
            int pos1 = random.nextInt(4);
            gens.set(pos1, freeCells.get(random.nextInt(freeCells.size() - 1)));
            fitnessScore = _fitness();
        }
    }

    public List<Point> getGens() {
        return gens;
    }

    @Override
    public int compareTo(Chromosome o) {
        return (int) (o.fitnessScore - fitnessScore);
    }
}

