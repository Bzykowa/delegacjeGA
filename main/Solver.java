package main;

import main.models.*;
import java.util.*;
import java.security.SecureRandom;

public class Solver {

    private final int populationSize = 20;
    private final int tournamentContestants = 5;
    private final int maxDays = 5;

    private ArrayList<Delegation> bestSolution = new ArrayList<>();
    private double bestFitness;
    private ArrayList<ArrayList<Delegation>> population = new ArrayList<>();
    private int maxDelegations = 0;
    private int maxMeals;

    private double optimalTotalCost;
    private ArrayList<Distance> distances;
    private double euro;

    public Solver(double optimalTotalCost, ArrayList<Distance> distances, int meals, double euro) {
        this.optimalTotalCost = optimalTotalCost;
        this.distances = distances;
        bestFitness = optimalTotalCost;
        maxMeals = meals;
        this.euro = euro;

        if (optimalTotalCost <= 1000) {
            maxDelegations = 1;
        } else if (optimalTotalCost <= 1800) {
            maxDelegations = 2;
        } else if (optimalTotalCost <= 2600) {
            maxDelegations = 3;
        } else if (optimalTotalCost <= 3400) {
            maxDelegations = 4;
        } else if (optimalTotalCost <= 4200) {
            maxDelegations = 5;
        } else {
            maxDelegations = 6;
        }

        generateInitialPopulation();
    }

    public ArrayList<ArrayList<Delegation>> getProposedSolutions() {
        return population;
    }

    public ArrayList<Delegation> getBestSolution() {
        return bestSolution;
    }

    // Deep copy
    private ArrayList<Delegation> cloneDelegations(ArrayList<Delegation> dels) {
        ArrayList<Delegation> newDels = new ArrayList<>();
        for (Delegation del : dels) {
            newDels.add(del.clone());
        }
        return newDels;
    }

    /**
     * Clone list of available Distances from initial data
     * 
     * @return List of all Distances
     */
    private ArrayList<Distance> generateDistancesList() {
        ArrayList<Distance> distancesList = new ArrayList<>();
        for (int i = 0; i < this.distances.size(); i++) {
            distancesList.add(this.distances.get(i).clone());
        }
        return distancesList;
    }

    /**
     * Generate random amount of days between minDays and maxDays
     * 
     * @param minDays
     * @return Random double value in specified range
     */
    private double generateRandomDays(double minDays) {
        SecureRandom rand = new SecureRandom();

        return (rand.nextInt((int) ((maxDays - minDays) * 2) + 1) + 2.0 * minDays) / 2.0;
    }

    /**
     * Generate random solution
     * 
     * @param distancesList
     * @return
     */
    private ArrayList<Delegation> randomSolution(ArrayList<Distance> distancesList) {
        SecureRandom rand = new SecureRandom();

        ArrayList<Delegation> proposedSolutionRandom = new ArrayList<>();
        ArrayList<Integer> usedPoints = new ArrayList<>();
        double previousFitness = optimalTotalCost;
        double currentFitness = checkFitness(proposedSolutionRandom);

        // Generate Delegations with distinct distances and stop when fitness drops
        // beneath 200 or starts to grow again (optimalTotalCost check is for the first
        // Delegation because we're starting from empty list and delegations amount
        // check is to not generate too many)
        int delCount = 0;
        while (currentFitness <= previousFitness && (currentFitness > 200 || currentFitness == optimalTotalCost)
                && delCount < maxDelegations) {

            int distanceIndex = rand.nextInt(distancesList.size());

            if (!distancesList.get(distanceIndex).isGermany()) {
                if (!usedPoints.contains(distanceIndex)) {
                    usedPoints.add(distanceIndex);
                } else {
                    continue;
                }
            }

            Delegation temp = new Delegation(distancesList.get(distanceIndex).clone());

            temp.setDaysWithDistanceCheck(generateRandomDays(0.5));
            temp.setRandomMealReductionWithMaxCheck(maxMeals);

            proposedSolutionRandom.add(temp);

            previousFitness = currentFitness;
            currentFitness = checkFitness(proposedSolutionRandom);
            delCount++;
        }
        return proposedSolutionRandom;
    }

    /**
     * Generate a few example answers for the algorithm to work with
     */
    private void generateInitialPopulation() {
        SecureRandom rand = new SecureRandom();
        ArrayList<Distance> distancesList = generateDistancesList();
        Collections.sort(distancesList);

        // If optimalCost is too low to build feasible solutions just get minimal
        // solution to population.
        if (optimalTotalCost < 80) {
            bestSolution.add(new Delegation(distancesList.get(0), 1, maxMeals));
            population.add(cloneDelegations(bestSolution));
            return;
        }

        for (int i = 0; i < populationSize / 2; i++) {
            ArrayList<Delegation> proposedSolutionDeterministic = new ArrayList<>();

            population.add(randomSolution(distancesList));

            // Generate deterministic solution
            int subDistanceListSize = distancesList.size() / maxDelegations;

            // Generate Delegations where number of them is decided by how many mean-sized
            // trips can worker make based on optimal cost
            // Then each Delegation is taken from another partition of availableDistances eg
            // (shortTrip, mediumLenghtTrip, longTrip)
            for (int j = 0; j < maxDelegations; j++) {

                int distanceIndex = rand.nextInt(subDistanceListSize) + j * subDistanceListSize;
                Delegation temp = new Delegation(distancesList.get(distanceIndex));

                temp.setDaysWithDistanceCheck(generateRandomDays(0.5));
                temp.setRandomMealReductionWithMaxCheck(maxMeals);

                proposedSolutionDeterministic.add(temp);
            }
            population.add(proposedSolutionDeterministic);
        }
        bestSolution = randomSolution(distancesList);
    }

    /**
     * Calculate cost of all delegations and how close it is to optimal cost
     * 
     * @param delegations
     * @return Absolute error of the cost of delegations and optimal cost
     */
    private double checkFitness(ArrayList<Delegation> delegations) {
        double currentCost = 0;

        for (Delegation delegation : delegations) {
            currentCost += delegation.delegationCost(euro);
        }

        return Math.abs(currentCost - optimalTotalCost);
    }

    /**
     * Calculate fitness function for every solution in population and update best
     * to date solution if needed
     * 
     * @return Array containing resuts of fitness function for every solution
     */
    private double[] calculateFitnesses() {
        double[] newFitnesses = new double[populationSize];
        ArrayList<Distance> distanceMonitor = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            newFitnesses[i] = checkFitness(population.get(i));

            // Penalty for repeating delegation and too many delegations
            int counter = 0;
            for (Delegation it : population.get(i)) {
                if (it.distance.isGermany()) {
                    continue;
                }
                counter++;
                if (!distanceMonitor.contains(it.distance)) {
                    distanceMonitor.add(it.distance);
                } else {
                    newFitnesses[i] += 10000;
                }
            }
            if (counter >= 3) {
                newFitnesses[i] += counter * 10;
            }

            distanceMonitor.clear();

            if (newFitnesses[i] < bestFitness) {
                bestFitness = newFitnesses[i];
                bestSolution = cloneDelegations(population.get(i));
            }
        }

        return newFitnesses;
    }

    /**
     * Select best solution from the set number of randomly selected solutions from
     * current population
     * 
     * @return Index of best solution found
     */
    private int linearTournament(double[] fitnesses) {
        SecureRandom rand = new SecureRandom();

        int bestIndex = rand.nextInt(populationSize);

        for (int i = 0; i < tournamentContestants; i++) {
            int candidate = rand.nextInt(populationSize);

            if (fitnesses[candidate] < fitnesses[bestIndex]) {
                bestIndex = candidate;
            }
        }

        return bestIndex;
    }

    /**
     * Swap one delegation from first solution with one from the second one
     * 
     * @param delegation1
     * @param delegation2
     * @return Two element arrayList containing changed values
     */
    private ArrayList<ArrayList<Delegation>> delegationCrossover(ArrayList<Delegation> delegation1,
            ArrayList<Delegation> delegation2) {

        ArrayList<ArrayList<Delegation>> results = new ArrayList<ArrayList<Delegation>>(2);
        SecureRandom rand = new SecureRandom();

        int maxSize = delegation1.size() >= delegation2.size() ? delegation2.size() : delegation1.size();
        int swapIndex = rand.nextInt(maxSize);

        ArrayList<Delegation> del1 = cloneDelegations(delegation1);
        ArrayList<Delegation> del2 = cloneDelegations(delegation2);

        Delegation swap = del1.get(swapIndex);

        del1.set(swapIndex, del2.get(swapIndex));
        del2.set(swapIndex, swap);

        results.add(del1);
        results.add(del2);

        return results;
    }

    /**
     * Swap duration of delegation between two random delegations from both
     * solutions
     * 
     * @param delegation1
     * @param delegation2
     * @return Two element arrayList containing changed values
     */
    private ArrayList<ArrayList<Delegation>> daysCrossover(ArrayList<Delegation> delegation1,
            ArrayList<Delegation> delegation2) {
        ArrayList<ArrayList<Delegation>> results = new ArrayList<ArrayList<Delegation>>(2);
        SecureRandom rand = new SecureRandom();

        int maxSize = delegation1.size() >= delegation2.size() ? delegation2.size() : delegation1.size();
        int swapIndex = rand.nextInt(maxSize);

        ArrayList<Delegation> del1 = cloneDelegations(delegation1);
        ArrayList<Delegation> del2 = cloneDelegations(delegation2);

        Delegation swap = del1.get(swapIndex);
        double daysSwap = swap.days;
        swap.setDaysWithDistanceCheck(del2.get(swapIndex).days);
        swap.setMealsReductionWithMaxCheck(maxMeals, swap.mealsReduction);
        del1.set(swapIndex, swap);

        swap = del2.get(swapIndex);
        swap.setDaysWithDistanceCheck(daysSwap);
        swap.setMealsReductionWithMaxCheck(maxMeals, swap.mealsReduction);
        del2.set(swapIndex, swap);

        results.add(del1);
        results.add(del2);

        return results;
    }

    /**
     * Swap meal reductions of delegation between two random delegations from both
     * solutions
     * 
     * @param delegation1
     * @param delegation2
     * @return Two element arrayList containing changed values
     */
    private ArrayList<ArrayList<Delegation>> mealsCrossover(ArrayList<Delegation> delegation1,
            ArrayList<Delegation> delegation2) {
        ArrayList<ArrayList<Delegation>> results = new ArrayList<ArrayList<Delegation>>(2);
        SecureRandom rand = new SecureRandom();

        int maxSize = delegation1.size() >= delegation2.size() ? delegation2.size() : delegation1.size();
        int swapIndex = rand.nextInt(maxSize);

        ArrayList<Delegation> del1 = cloneDelegations(delegation1);
        ArrayList<Delegation> del2 = cloneDelegations(delegation2);

        Delegation swap = del1.get(swapIndex);
        int mealsSwap = swap.mealsReduction;
        swap.setMealsReductionWithMaxCheck(maxMeals, del2.get(swapIndex).mealsReduction);
        del1.set(swapIndex, swap);

        swap = del2.get(swapIndex);
        swap.setMealsReductionWithMaxCheck(maxMeals, mealsSwap);
        del2.set(swapIndex, swap);

        results.add(del1);
        results.add(del2);

        return results;
    }

    /**
     * Randomly change amount of company funded meals in one delegation
     * 
     * @param delegation
     * @return Mutated delegation
     */
    private Delegation mealsMutation(Delegation delegation) {

        delegation.setRandomMealReductionWithMaxCheck(maxMeals);

        return delegation;
    }

    /**
     * Randomly change duration of one delegation
     * 
     * @param delegation
     * @return Mutated delegation
     */
    private Delegation daysMutation(Delegation delegation) {
        delegation.setDaysWithDistanceCheck(generateRandomDays(0.5));
        delegation.setMealsReductionWithMaxCheck(maxMeals, delegation.mealsReduction);

        return delegation;
    }

    /**
     * Merges two delegations which have lowest distances
     * 
     * @param delegations
     * @return Mutated list of delegations
     */
    private ArrayList<Delegation> mergeMutation(ArrayList<Delegation> delegations) {

        ArrayList<Distance> availableDistances = generateDistancesList();
        ArrayList<Distance> takenDistances = new ArrayList<>();
        Delegation min = null;
        Delegation min2 = null;
        ArrayList<Delegation> dels = cloneDelegations(delegations);

        // Find already taken distances and find two shortest delegations
        for (Delegation delegation : dels) {
            takenDistances.add(delegation.distance);
            if (min == null || min.distance.getFullKilometers() >= delegation.distance.getFullKilometers()) {
                min2 = min;
                min = delegation;
            } else if (min2 == null || min2.distance.getFullKilometers() >= delegation.distance.getFullKilometers()) {
                min2 = delegation;
            }
        }

        // If unable to find two minimums end here
        if (min2 == null)
            return dels;

        // Remove taken Distances
        for (Distance distance : takenDistances) {
            availableDistances.removeIf(d -> d.start == distance.start && d.end == distance.end);
        }

        Distance newDistance;

        // If all points are in use set new Delegation distance to min2.distance else
        // filter further
        if (availableDistances.isEmpty()) {
            newDistance = min2.distance;
        } else {
            // Save max in case everything gets filtered out
            Collections.sort(availableDistances);
            Distance max = availableDistances.get(availableDistances.size() - 1);

            // Remove distances shorter than 2 times min
            final double minDistance = 2 * min.distance.getFullKilometers();
            availableDistances.removeIf(d -> d.getFullKilometers() <= minDistance);

            newDistance = availableDistances.isEmpty() ? max : availableDistances.get(0);
        }

        // Merge delegations to one
        Delegation merged = new Delegation(newDistance);
        merged.setDaysWithDistanceCheck(min.days);
        merged.setMealsReductionWithMaxCheck(maxMeals, min2.mealsReduction);

        dels.add(merged);
        dels.remove(min);
        dels.remove(min2);

        return dels;
    }

    /**
     * Split highest distance delegation into two
     * 
     * @param delegations
     * @return Mutated list of delegations
     */
    private ArrayList<Delegation> splitMutation(ArrayList<Delegation> delegations) {

        // If current number of delegations is equal or higher than max end here
        if (delegations.size() >= maxDelegations) {
            return delegations;
        }

        ArrayList<Distance> availableDistances = generateDistancesList();
        ArrayList<Distance> takenDistances = new ArrayList<>();
        Delegation max = null;
        ArrayList<Delegation> dels = cloneDelegations(delegations);

        // Find already taken distances and find maximum
        for (Delegation delegation : dels) {
            takenDistances.add(delegation.distance);
            if (max == null || max.distance.getFullKilometers() <= delegation.distance.getFullKilometers()) {
                max = delegation;
            }
        }

        // Remove taken Distances
        for (Distance distance : takenDistances) {
            availableDistances.removeIf(d -> d.start == distance.start && d.end == distance.end);
        }

        // Sort availableDistances to easily get max
        Collections.sort(availableDistances);

        try {
            Distance min = availableDistances.get(0);
            Distance min2 = availableDistances.get(1);

            final double maxDistance = max.distance.getFullKilometers() / 2;

            // Remove distances longer than max distance by 2
            availableDistances.removeIf(d -> d.getFullKilometers() >= maxDistance);

            // Get min or two longest of remaining available distances
            Distance newDistance = availableDistances.isEmpty() ? min
                    : availableDistances.get(availableDistances.size() - 1);
            Distance newDistance2 = availableDistances.size() < 2 ? min2
                    : availableDistances.get(availableDistances.size() - 2);

            // Merge delegations to one
            Delegation split = new Delegation(newDistance);
            Delegation split2 = new Delegation(newDistance2);
            split.setDaysWithDistanceCheck(max.days);
            split2.setDaysWithDistanceCheck(max.days);
            split.setMealsReductionWithMaxCheck(maxMeals, max.mealsReduction);
            split2.setMealsReductionWithMaxCheck(maxMeals, max.mealsReduction);
            dels.add(split);
            dels.add(split2);
        } catch (IndexOutOfBoundsException e) {
            return dels;
        }

        dels.remove(max);

        return dels;
    }

    /**
     * Run genetic algorithm on generated population in search for better solution.
     * Stop when fitness < epsilon or time runs out.
     * 
     * Probabilities used:
     * 
     * -crossover: -- delegationCrossover = 50% -- daysCrossover = 25% --
     * mealsCrossover = 25%
     * 
     * -mutation: (20%) -- daysMutation = 35% -- mealsMutation = 35% --
     * mergeMutation = 15% -- splitMutation = 15%
     * 
     * @param milliseconds
     * @param epsilon
     * @return Best solution found
     */
    public ArrayList<Delegation> solve(int milliseconds, double epsilon) {

        if (population.size() < populationSize) {
            return population.get(0);
        }

        SecureRandom rand = new SecureRandom();
        long startTime = System.nanoTime();
        long endTime = System.nanoTime();
        ArrayList<Distance> distancesList = generateDistancesList();

        while ((endTime - startTime) / 1000000 < milliseconds + 1) {
            double[] fitnesses = calculateFitnesses();

            if (bestFitness < epsilon) {
                break;
            }

            ArrayList<ArrayList<Delegation>> newPopulation = new ArrayList<ArrayList<Delegation>>(populationSize);

            for (int i = 0; i < populationSize / 2 - 1; i++) {
                ArrayList<Delegation> parent1 = population.get(linearTournament(fitnesses));
                ArrayList<Delegation> parent2 = population.get(linearTournament(fitnesses));

                int crossover = rand.nextInt(100);
                ArrayList<ArrayList<Delegation>> children;

                if (crossover < 50) {
                    children = delegationCrossover(parent1, parent2);
                } else if (crossover < 75) {
                    children = daysCrossover(parent1, parent2);
                } else {
                    children = mealsCrossover(parent1, parent2);
                }

                newPopulation.add(children.get(0));
                newPopulation.add(children.get(1));
            }

            // To avoid converging of population add bestSolution and one random
            newPopulation.add(cloneDelegations(bestSolution));
            newPopulation.add(randomSolution(distancesList));

            int mutation = rand.nextInt(100);

            // Normal mutation on new population
            if (mutation < 20) {

                for (int i = rand.nextInt(7); i < 7; i++) {
                    mutation = rand.nextInt(100);

                    if (mutation < 15) {
                        mutation = rand.nextInt(populationSize);
                        newPopulation.set(mutation, mergeMutation(newPopulation.get(mutation)));
                    } else if (mutation < 50) {
                        mutation = rand.nextInt(populationSize);
                        ArrayList<Delegation> mutatingSolution = newPopulation.get(mutation);
                        int delegationIndex = rand.nextInt(mutatingSolution.size());
                        mutatingSolution.set(delegationIndex, daysMutation(mutatingSolution.get(delegationIndex)));
                        newPopulation.set(mutation, mutatingSolution);
                    } else if (mutation < 85) {
                        mutation = rand.nextInt(populationSize);
                        ArrayList<Delegation> mutatingSolution = newPopulation.get(mutation);
                        int delegationIndex = rand.nextInt(mutatingSolution.size());
                        mutatingSolution.set(delegationIndex, mealsMutation(mutatingSolution.get(delegationIndex)));
                        newPopulation.set(mutation, mutatingSolution);
                    } else {
                        mutation = rand.nextInt(populationSize);
                        newPopulation.set(mutation, splitMutation(newPopulation.get(mutation)));
                    }
                }
            }

            population = newPopulation;
            endTime = System.nanoTime();
        }

        return bestSolution;
    }

}