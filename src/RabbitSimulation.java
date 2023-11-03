package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import src.predators.Predator;
import src.rabbits.*;
public class RabbitSimulation {
    private List<Rabbit> population;
    private List<Predator> predators;
    private int currentYear;
    private int simulationMonths;

    public RabbitSimulation(int initialAdult, int initialPredators, int simulationMonths) {
        this.currentYear = 0;
        this.simulationMonths = simulationMonths;
        this.population = new ArrayList<>();
        this.predators = new ArrayList<>();

        for (int i = 0; i < initialAdult; i++) {
            population.add(new Rabbit(0, Sex.MALE));
            population.add(new Rabbit(0, Sex.FEMALE));
        }

        for (int i = 0; i < initialPredators; i++) {
            predators.add(new Predator());
        }
    }

    public void runSimulation() 
    {
        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[]{0x123, 0x234, 0x345, 0x456});

        List<Integer> populationData = new ArrayList<>();
        List<Integer> predatorData   = new ArrayList<>();
        List<Integer> babyRabbitData = new ArrayList<>();

        int births;
        int deaths;
        int numberOfShotsByPredators;

        int totalBirths;
        int totalDeaths;
        int totalPregnancy;
        int totalShotsByPredators;

        int totalFemales;
        int totalMales;
        
        int initialPopulationSize = population.size();

        List<Rabbit> newPopulation;
        List<Predator> newPredators;

        totalBirths = 0;
        totalDeaths = 0;
        totalPregnancy = 0;
        totalShotsByPredators = 0;

        totalFemales = 0;
        totalMales = 0;

        while (currentYear < simulationMonths && population.size() != 0) {
            currentYear++;
            births = 0;
            deaths = 0;
            numberOfShotsByPredators = 0;

            totalFemales = 0;
            totalMales   = 0;

            newPopulation = new ArrayList<>();
            newPredators  = new ArrayList<>();

            for (Predator predator : predators) {
                predator.incrementAge();
                if (predator.isAlive(rnd)) 
                {
                    numberOfShotsByPredators += predator.hunt(population, rnd);
                    newPredators.add(predator);
                }
            }

            deaths+=numberOfShotsByPredators;
            totalShotsByPredators += numberOfShotsByPredators;
            predators = newPredators;

            for (Rabbit rabbit : population) {
                rabbit.incrementAge();

                if (rabbit.isAlive(rnd)) {
                    newPopulation.add(rabbit);

                    if (rabbit.canGiveBirth()) {
                        rabbit.gettingPregnant();
                        totalPregnancy++;
                        List<Rabbit> newKittens = rabbit.giveBirth(rnd);
                        newPopulation.addAll(newKittens);
                        births += newKittens.size();
                    }
                } else {
                    deaths++;
                }
            }

            if (newPopulation.isEmpty()) {
                System.out.println("\n!! Population has gone extinct in month " + currentYear + "!!\n");
                break;
            } else {
                for (Rabbit rabbit : newPopulation) {
                    if (rabbit.getSex() == Sex.MALE) {
                        totalMales++;
                    } else if (rabbit.getSex() == Sex.FEMALE) {
                        totalFemales++;
                    } 
                }
            }

            totalBirths += births;
            totalDeaths += deaths;

            population = newPopulation;

            populationData.add(population.size());
            predatorData.add(predators.size());
            babyRabbitData.add(births);

            System.out.println(String.format("\n* Month %d: Births = %d, Deaths = %d, Population = %d", currentYear, births, deaths, newPopulation.size()));
            System.out.println("-- Number of shots :\t" + numberOfShotsByPredators);
            System.out.println("-- Number of natural deaths:\t" + Math.abs(deaths - numberOfShotsByPredators));
            System.out.println("-------------------------------------------------");
            System.out.println("Total females : \t" + totalFemales + " / Total males : \t" + totalMales);
            System.out.println("-------------------------------------------------");
        }
        RSimulationStats stats = new RSimulationStats(totalBirths, totalDeaths, totalShotsByPredators, totalPregnancy, totalFemales, totalMales, initialPopulationSize, population.size());
        stats.display(simulationMonths);

        JFrame graphFrame = new JFrame("Rabbit Population Graph");
        graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graphFrame.setSize(800, 400);

        // Créez une instance de RabbitPopulationGraph et passez-lui les données de population, de prédateurs et de bébés lapins
        RabbitPopulationGraph graph = new RabbitPopulationGraph(populationData, predatorData, babyRabbitData);

        // Ajoutez le graphique à la fenêtre
        graphFrame.add(graph);
        graphFrame.setVisible(true);
    }
}
