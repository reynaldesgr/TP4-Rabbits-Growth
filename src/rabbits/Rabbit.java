/**
 * The Rabbit class represents an individual rabbit in the population simulation.
 * It contains information about the rabbit's age, gender, survival rate, and life status.
 * The class also includes methods for giving birth, calculating survival rates,
 * and determining the average number of litters per year for mature females.
 * 
 * @author SEGERIE Reynalde
 */
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

    /**
     * Constructs a new Rabbit instance with the specified age, gender, and a random number generator.
     *
     * @param age       the age of the rabbit
     * @param isFemale  true if the rabbit is female, false otherwise
     * @param rnd       the random number generator
     */

    public Rabbit(int age, boolean isFemale, MTRandom rnd) 
    {
        this.age = age;
        this.isFemale = isFemale;
        this.alive    = (rnd.nextDouble() < calculateSurvivalRate(age));
    }

    /**
     * Checks if the rabbit is alive.
     *
     * @return true if the rabbit is alive, false otherwise
     */

    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Checks if the rabbit is female.
     *
     * @return true if the rabbit is female, false otherwise
     */

    public boolean isFemale() 
    {
        return isFemale;
    }

    /**
     * Gets the survival rate of the rabbit.
     *
     * @return the survival rate of the rabbit
     */

    public double getSurvivalRate() 
    {
        return survivalRate;
    }

    /**
     * Generates a list of new rabbits representing a birth event.
     *
     * @param numberOfKittens the number of kittens to be born
     * @param random          the random number generator
     * @return                a list of new rabbits born
     */

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

    /**
     * Calculates the survival rate based on the age of the rabbit.
     *
     * @param age the age of the rabbit
     * @return    the calculated survival rate
     */

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
            return Math.max(0, 0.6 - ( (age - 10) * SURVIVAL_DECREASE_RATE ) );
        }
    }

    /**
     * Calculates the number of litters for a single rabbit.
     *
     * @param rnd the random number generator
     * @return the number of litters
     */

     public static int calculateNumberOfLitters(MTRandom rnd) 
     {
        int numLitters;
        double rndNumber = rnd.nextDouble();
    
        // A female rabbit gives birth to 4 to 8 litters per year
        // Non-Uniform law
        
        if (rndNumber < 0.1) 
        {
            // 10% chance for 4 litters
            numLitters = 4;
        } 
        else if (rndNumber < 0.4) 
        {
            // 30% chance for 5 litters
            numLitters = 5;
        } 
        else if (rndNumber < 0.7) 
        {
            // 30% chance for 6 litters
            numLitters = 6;
        } 
        else if (rndNumber < 0.9) 
        {
            // 20% chance for 7 litters
            numLitters = 7;
        } 
        else 
        {
            // 10% chance for 8 litters
            numLitters = 8;
        }
    
        return numLitters;
    }
    
    /**
     * Gets the age of the rabbit.
     *
     * @return the age of the rabbit
     */
    
    public int getAge()
    {
        return this.age;
    }
}
