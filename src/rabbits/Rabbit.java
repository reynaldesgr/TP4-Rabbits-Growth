package src.rabbits;

import java.util.ArrayList;
import java.util.List;

import src.MTRandom;

public class Rabbit 
{
    private static final double IMMATURE_SURVIVAL_RATE = 0.35;
    private static final double ADULT_SURVIVAL_RATE = 0.60;
    private static final double SURVIVAL_DECREASE_RATE = 0.1;

    private boolean isFemale;
    private int age;
    private double survivalRate;
    private boolean alive;

    public Rabbit(int age, boolean isFemale) 
    {
        this.age = age;
        this.isFemale = isFemale;
        updateSurvivalAndAlive();
    }

    public void incrementAge()
    {
        this.age++;
        updateSurvivalAndAlive();
    }

    public boolean isAlive()
    {
        return alive;
    }

    private void updateSurvivalAndAlive() 
    {
        if (age < 5) {
            // 35% chance of survival for immature rabbits (less than 5 months)
            survivalRate = 0.35;
        } else if (age >= 5 && age <= 8) 
        {
            // Survival rate of adults (between 5 and 8 months)
            survivalRate = 0.60;
        } 
        else 
        {
            // After the age of ten years, the survival rate decreases by 10% each year
            if (age > 10) 
            {
                survivalRate -= 0.1;
            }
        }

        // Check if the rabbit is still alive based on survival rate
        alive = survivalRate > 0;
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
            kittens.add(new Rabbit(0, isFemale));
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
}
