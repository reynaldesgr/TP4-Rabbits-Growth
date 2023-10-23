package src;

import java.util.ArrayList;
import java.util.List;
import src.rabbits.Rabbit;
import src.rabbits.Sex;

public class RabbitSimulation {
    private List<Rabbit> population;
    private int currentYear;
    private int simulationMonth;

    public RabbitSimulation(int initialAdult, int simulationMonth) 
    {
        this.currentYear = 0;
        this.simulationMonth = simulationMonth;

        this.population = new ArrayList<>();

        for (int i = 0; i < initialAdult; i++)
        {
            population.add(new Rabbit(0, Sex.MALE));
            population.add(new Rabbit(0, Sex.FEMALE));            
        }
        
    }

    public void runSimulation() 
    {
        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[] { 0x123, 0x234, 0x345, 0x456 });

        while (currentYear < simulationMonth) 
        {
            currentYear++;
            int births = 0;
            int deaths = 0;
            List<Rabbit> newPopulation = new ArrayList<>();

            for (Rabbit rabbit : population) 
            {
                rabbit.incrementAge();

                if (rabbit.isAlive(rnd)) 
                {
                    newPopulation.add(rabbit);

                    if (rabbit.canGiveBirth()) 
                    {
                        rabbit.gettingPregnant();
                        List<Rabbit> newKittens = rabbit.giveBirth(rnd);
                        newPopulation.addAll(newKittens);
                        births += newKittens.size();
                    }
                } else 
                {
                    deaths++;
                }
            }
            population = newPopulation;

            System.out.println(String.format("Month %d: Births=%d, Deaths=%d, Population=%d", currentYear, births, deaths, newPopulation.size()));

            
        }
    }
}
