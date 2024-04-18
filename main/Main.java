package main;

import java.time.Duration;
import java.util.ArrayList;

import main.models.Delegation;
import main.models.Distance;
import main.models.Pair;

public class Main {
    public static void main(String args[]) {

        double optimalCost = Double.parseDouble(args[0]);
        int timeMillis = Integer.parseInt(args[1]);
        double epsilon = Double.parseDouble(args[2]);
        int meals = Integer.parseInt(args[3]);

        // Mocked data
        ArrayList<Distance> distances = new ArrayList<Distance>();
        distances.add(
                new Distance(
                        new Pair<Double, Double>(535.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(19620), Duration.ZERO),
                        0,
                        -1,
                        1,
                        "Wroclaw",
                        "",
                        "Bialystok"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(376.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(14520), Duration.ZERO),
                        0,
                        -1,
                        2,
                        "Wroclaw",
                        "",
                        "Stargard"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(443.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(20160), Duration.ZERO),
                        0,
                        -1,
                        3,
                        "Wroclaw",
                        "",
                        "Slupsk"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(334.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(13800), Duration.ZERO),
                        0,
                        -1,
                        4,
                        "Wroclaw",
                        "",
                        "Torun"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(357.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(15000), Duration.ZERO),
                        0,
                        -1,
                        5,
                        "Wroclaw",
                        "",
                        "Radom"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(164.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(6600), Duration.ZERO),
                        0,
                        -1,
                        6,
                        "Wroclaw",
                        "",
                        "Zgorzelec"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(256.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(11280), Duration.ZERO),
                        0,
                        -1,
                        7,
                        "Wroclaw",
                        "",
                        "Drezdenko"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(42.5, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(3420), Duration.ZERO),
                        0,
                        -1,
                        8,
                        "Wroclaw",
                        "",
                        "Brzeg"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(188.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(7680), Duration.ZERO),
                        0,
                        -1,
                        9,
                        "Wroclaw",
                        "",
                        "Rybnik"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(437.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(15660), Duration.ZERO),
                        0,
                        -1,
                        10,
                        "Wroclaw",
                        "",
                        "Rzeszow"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(506.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(19800), Duration.ZERO),
                        0,
                        -1,
                        11,
                        "Wroclaw",
                        "",
                        "Gdynia"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(200.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(9660), Duration.ZERO),
                        0,
                        -1,
                        12,
                        "Wroclaw",
                        "",
                        "Czestochowa"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(127.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(7320), Duration.ZERO),
                        0,
                        -1,
                        13,
                        "Wroclaw",
                        "",
                        "Miedzylesie"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(82.4, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(4500), Duration.ZERO),
                        0,
                        -1,
                        14,
                        "Wroclaw",
                        "",
                        "Walbrzych"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(363.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(13500), Duration.ZERO),
                        0,
                        -1,
                        15,
                        "Wroclaw",
                        "",
                        "Tarnow"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(217.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(8820), Duration.ZERO),
                        0,
                        -1,
                        16,
                        "Wroclaw",
                        "",
                        "Lodz"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(102.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(5760), Duration.ZERO),
                        0,
                        -1,
                        17,
                        "Wroclaw",
                        "",
                        "Ostrow Wielkopolski"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(187.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(7560), Duration.ZERO),
                        0,
                        -1,
                        18,
                        "Wroclaw",
                        "",
                        "Zielona Gora"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(135.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(6240), Duration.ZERO),
                        0,
                        -1,
                        19,
                        "Wroclaw",
                        "",
                        "Kedzierzyn-Kozle"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(125.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(5700), Duration.ZERO),
                        0,
                        -1,
                        20,
                        "Wroclaw",
                        "",
                        "Wielun"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(507.0, 0.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(21000), Duration.ZERO),
                        0,
                        -1,
                        21,
                        "Wroclaw",
                        "",
                        "Olsztyn"));
        distances.add(
                new Distance(
                        new Pair<Double, Double>(184.0, 499.0),
                        new Pair<Duration, Duration>(Duration.ofSeconds(6900), Duration.ofSeconds(19200)),
                        0,
                        23,
                        22,
                        "Wroclaw",
                        "Olszyna",
                        "Itzehoe"));

        double euroPrice = 4.3;

        Solver optimalDelegations = new Solver(optimalCost, distances, meals, euroPrice);

        ArrayList<Delegation> optimal = optimalDelegations.solve(timeMillis, epsilon);

        double total = 0;
        System.out.println("---------------------------------------");
        for (Delegation delegation : optimal) {
            total += delegation.delegationCost(euroPrice);
            System.out.println("cost: " + delegation.delegationCost(euroPrice) + "; " + delegation.toString());
        }
        System.out.println("TOTAL: " + total);
        System.out.println("---------------------------------------");
        /*
         * System.out.println("---------------------------------------");
         * 
         * 
         * 
         * 
         * for(ArrayList<Delegation> delegations :
         * optimalDelegations.getProposedSolutions()){
         * total = 0;
         * for(Delegation delegation : delegations){
         * total += delegation.delegationCost();
         * System.out.println("cost: " + delegation.delegationCost() + "; "+
         * delegation.toString());
         * }
         * System.out.println("TOTAL: " + total);
         * System.out.println("---------------------------------------");
         * }
         */
    }
}
