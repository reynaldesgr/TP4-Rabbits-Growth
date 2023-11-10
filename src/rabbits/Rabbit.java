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
            boolean isFemale = random.nextBoolean();
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

    public int getAge()
    {
        return this.age;
    }
}
