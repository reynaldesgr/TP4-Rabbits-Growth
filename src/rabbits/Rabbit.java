package src.rabbits;

import java.util.ArrayList;
import java.util.List;

import src.MTRandom;

public class Rabbit 
{
    private static final double IMMATURE_SURVIVAL_RATE = 0.35;
    private static final double ADULT_SURVIVAL_RATE    = 0.60;
    private static final double SURVIVAL_DECREASE_RATE = 0.1;

    private boolean isFemale;
    private int     age;
    private double  survivalRate;
    private boolean alive;

    public Rabbit(int age, boolean isFemale, MTRandom rnd) 
    {
        this.age = age;
        this.isFemale = isFemale;
        this.alive    = (rnd.nextDouble() < calculateSurvivalRate(age));
    }

    public boolean isAlive()
    {
        return alive;
    }

    public boolean isFemale() 
    {
        return isFemale;
    }

    public double getSurvivalRate() 
    {
        return survivalRate;
    }

    public static List<Rabbit> giveBirth(int numberOfKittens, MTRandom random)
    {
        List<Rabbit> kittens = new ArrayList<>();

        for (int i = 0; i < numberOfKittens; i++) 
        {
            double  rnd = Math.random();
            boolean isFemale;
            
            if (rnd < 0.5)
            {
                isFemale = false;
            }
            else
            {
                isFemale = true;
            }
            
            Rabbit  newBorn  = new Rabbit(0, isFemale, random);
            if (newBorn.isAlive())
            {
                kittens.add(newBorn);
            }
        }
        return kittens;
    }

    public static double calculateSurvivalRate(int age) 
    {
        if (age < 5) 
        {
            return IMMATURE_SURVIVAL_RATE;
        } 
        else if (age >= 5 && age <= 10) 
        {
            return ADULT_SURVIVAL_RATE;
        } 
        else 
        {
            return Math.max(0, 0.6 - ( (10 - age) * SURVIVAL_DECREASE_RATE ) );
        }
    }
    
    public static int calculateNumberOfLitters(MTRandom rnd)
    {
        int numLitters;
        double rndNumber = rnd.nextDouble();

        if (rndNumber < 0.1)
        {
            numLitters = 3;
        }
        else if (rndNumber < 0.4)
        {
            numLitters = 4;
        }
        else if (rndNumber < 0.8)
        {
            numLitters = 5;
        }
        else
        {
            numLitters = 6;
        }

        return numLitters;
    }
    
    private static int calculateTotalLitters(MTRandom rnd, long numMatureFemales) 
    {
        int totalLitters = 0;
    
        for (long i = 0; i < numMatureFemales; i++) 
        {
            int numLitters = calculateNumberOfLitters(rnd);
            totalLitters += numLitters;
        }
    
        return totalLitters;
    }
    
    public static double calculateAverageLittersPerYear(MTRandom rnd, long numMatureFemales) 
    {
        int totalLitters = calculateTotalLitters(rnd, numMatureFemales);
        return (double) totalLitters / numMatureFemales;
    }

    public int getAge()
    {
        return this.age;
    }
}
