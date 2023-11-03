package src.predators;

import java.util.List;
import java.util.Random;

import src.MTRandom;
import src.rabbits.Rabbit;

public class Predator 
{
    private int ageInMonths;
    private double survivalRate;

    public Predator()
    {
        ageInMonths  = 0;
        survivalRate = 0.8; // Initial survival rate
    }

    public void incrementAge()
    {
        ageInMonths++;

        if (ageInMonths > 5)
        {
            survivalRate-= 0.5;
        }
    }


    public boolean isAlive(MTRandom random)
    {
        double randomNumber = random.nextDouble();
        return randomNumber <= survivalRate;
    }


    public int hunt(List<Rabbit> rabbits, MTRandom random)
    {
        int i;
        int numberOfShots = 0;

        for (i = rabbits.size() - 1; i >= 0; i--) 
        {
            Rabbit rabbit = rabbits.get(i);
            if (random.nextDouble() < 0.02) 
            {
                // Le prédateur a une probabilité de 2% de chasser un lapin
                if (rabbit.isAlive(random)) 
                {
                    // Le lapin est chassé
                    rabbits.remove(i);
                    numberOfShots++;
                }
            }
        }
        return numberOfShots;
    }
}