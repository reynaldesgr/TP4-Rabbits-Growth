package src;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import src.predators.Predator;
import src.rabbits.*;

public class RabbitSimulation {
    private List<Rabbit> population;
    private List<Predator> predators;
    private int currentMonth;
    private int simulationMonths;

    public RabbitSimulation(int initialAdult, int initialPredators, int simulationMonths) {
        this.currentMonth = 0;
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

    public void runSimulation() {
        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[]{0x124, 0x234, 0x341, 0x450});

        int births = 0;
        int deaths = 0;
        int numberOfShotsByPredators = 0;
        int females, males;

        ArrayList<Rabbit> tmp;

        while (currentMonth < simulationMonths) {
            currentMonth++;

            females                  = 0;
            males                    = 0;
            births                   = 0;
            deaths                   = 0;
            numberOfShotsByPredators = 0;
            
            tmp = new ArrayList<>(population);
            
            for (Predator predator : predators) 
            {
                if (predator.isAlive(rnd)) 
                {
                    predator.incrementAge();                    
                    numberOfShotsByPredators += predator.hunt(population, rnd);
                }
            }

            deaths                += numberOfShotsByPredators;


            for (Rabbit rabbit : tmp) 
            {

                if (rabbit.isAlive(rnd)) 
                {
                    rabbit.incrementAge();

                    if (rabbit.canGiveBirth()) 
                    {
    
                        if (rabbit.isPregnant()) 
                        {
                            population.addAll(rabbit.giveBirth(rnd));
                            births ++;
                        } 
                        else 
                        { 
                            rabbit.gettingPregnant();
                        }
                    }
                } 
                else 
                {
                    deaths++;
                }
            }

            if (population.isEmpty()) 
            {
                System.out.println("\n!! Population has gone extinct in year " + currentMonth + "!!\n");
                break;
            } 
            else 
            {
                for (Rabbit rabbit : population) 
                {
                    if (rabbit.getSex() == Sex.MALE) 
                    {
                        males++;
                    } 
                    else if (rabbit.getSex() == Sex.FEMALE) 
                    {
                        females++;
                    }
                }
            }

            System.out.println(String.format("\n* Year %d: Births = %d, Deaths = %d, Population = %d", currentMonth, births, deaths, population.size()));
            System.out.println("-- Number of shots :\t" + numberOfShotsByPredators);
            System.out.println("-- Number of natural deaths:\t" + Math.abs(deaths - numberOfShotsByPredators));
            System.out.println("-------------------------------------------------");
            System.out.println("Total females : \t" + females + " / Total males : \t" + males);
            System.out.println("\n-------------------------------------------------");
        }
    }
}
