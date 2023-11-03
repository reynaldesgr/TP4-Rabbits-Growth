package src;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import src.predators.Predator;
import src.rabbits.*;

public class RabbitSimulation {
    private List<Rabbit> population;
    private List<Predator> predators;
    private int currentYear;
    private int simulationYear;

    public RabbitSimulation(int initialAdult, int initialPredators, int simulationYear) {
        this.currentYear    = 0;
        this.simulationYear = simulationYear;
        this.population     = new ArrayList<>();
        this.predators      = new ArrayList<>();

        for (int i = 0; i < initialAdult; i++) {
            population.add(new Rabbit(0, Sex.MALE));
            population.add(new Rabbit(0, Sex.FEMALE));
        }

        for (int i = 0; i < initialPredators; i++) {
            predators.add(new Predator());
        }
    }

    public void runSimulation() {
        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[]{0x124, 0x234, 0x341, 0x450});
        long deaths, shots, births, females, males;
        int size = population.size();
        
        while (currentYear < simulationYear) {
            currentYear++;

            System.out.println("Initial population at year " + currentYear + " : " + size);
            deaths  = 0;
            shots   = 0;
            births  = 0;
            females = 0;
            males   = 0;
            size    = 0;
            List<Rabbit> newPopulation = new ArrayList<>();

            // Update predator age and check for survival
            for (Predator predator : predators) 
            {
                predator.incrementAge();
            }

            // Update rabbit age and check for survival

            for (Rabbit rabbit : population)
            {
                rabbit.survive(rnd);
            }

            for (Rabbit rabbit : population) {
                if (rabbit.isAlive())
                {
                    rabbit.incrementAge();
                }
                else
                {
                    deaths++;
                }
            }

            // Predators hunt rabbits
            for (Predator predator : predators) {
                if (predator.isAlive(rnd))
                {
                    shots += predator.hunt(population, rnd);
                }
            }

            // Check for rabbit reproduction
            List<Rabbit> newKittens = new ArrayList<>();
            for (Rabbit rabbit : population) {
                if (rabbit.isAlive())
                {
                    if (rabbit.canGiveBirth() && rabbit.isPregnant()) {
                        newKittens.addAll(rabbit.giveBirth(rnd));
                        births++;
                    }
                    else if (!rabbit.isPregnant())
                    {
                        rabbit.gettingPregnant();
                    }
                }
            }

            population.addAll(newKittens);

            for (Rabbit rabbit : population)
            {
                if (rabbit.isAlive())
                {
                    if (rabbit.getSex() == Sex.FEMALE)
                    {
                        females++;
                    }
                    else
                    {
                        males++;
                    }
                    size++;
                }
            }
            System.out.println("--------------------------------------------");
            System.out.println("* Year "        + currentYear);
            System.out.println("- Females : "   + females);
            System.out.println("- Males : "     + males);
            System.out.println("- Natural Deaths : "          + deaths);
            System.out.println("- Death(s) by predator(s) : " + shots);
            System.out.println("- Births : "    + births);
            System.out.println("--------------------------------------------");
        }
    }
}
