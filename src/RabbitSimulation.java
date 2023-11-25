
/**
 * The RabbitSimulation class simulates the population growth of rabbits over several years.
 * It uses a simple model to estimate the number of births and simulate the survival of rabbits
 * based on their age and gender.
 *
 * @author SEGERIE Reynalde
 */

package src;

import src.rabbits.Rabbit;

import java.util.List;

public class RabbitSimulation 
{
    private long[] females = new long[15];
    private long[] males   = new long[15];
    private int simulationYear;

    private static final long ESTIMATION_THRESHOLD = 1000000;
    private static final double[] CONSTANT_PREDATION_PROBABILITIES = {0.7, 0.5, 0.25, 0.35, 0.4, 0.2, 0.3, 0.25, 0.35, 0.4, 0.2, 0.3, 0.25, 0.35, 0.4};

    /**
     * Constructs a RabbitSimulation object with the specified initial number of females, males, and simulation years.
     *
     * @param initialFemales the initial number of female rabbits
     * @param initialMales   the initial number of male rabbits
     * @param simulationYear the number of years to simulate
     */

    public RabbitSimulation(int initialFemales, int initialMales, int simulationYear) 
    {
        this.simulationYear = simulationYear;

        // Mature rabbits
        females[1] = initialFemales;
        males  [1] = initialMales;
    }

    /**
     * Estimates the number of babies based on the number of mature females, mature males, and a random number generator.
     *
     * @param numMatureFemales the number of mature female rabbits
     * @param numMatureMales   the number of mature male rabbits
     * @param rnd              the random number generator
     * @return                 an array containing the estimated number of female and male babies
     */

     private long[] estimateNumberOfBabies(long numMatureFemales, long numMatureMales, MTRandom rnd) 
     {
        // Base factor for estimating the number of offspring per mature female
        double baseFactor = 4.8;
    
        // Additional factor based on male availability
        double maleAvailabilityFactor = Math.min(1.0, (double) numMatureMales / numMatureFemales);
    
        double randomFactor = 0.4 + rnd.nextDouble() * 0.08;
    
        // Final estimation using the factors
        double estimatedNumBabies = (baseFactor * maleAvailabilityFactor * randomFactor) * numMatureFemales;
    
        // Estimate the number of births for males and females
        long estimatedNumFemales = (long) (estimatedNumBabies * 0.5); // 50% females
        long estimatedNumMales   = (long) (estimatedNumBabies * 0.5); // 50% males
    
        estimatedNumFemales = Math.max(0, estimatedNumFemales);
        estimatedNumMales   = Math.max(0, estimatedNumMales);
    
        // Return an array with estimates for males and females
        return new long[]{estimatedNumFemales, estimatedNumMales};
    }
    
    

    /**
     * Runs the rabbit population simulation for the specified number of years.
     *
     * @param stats the RSimulationStats object to store simulation statistics
     */

    public void runSimulation(RSimulationStats stats) 
    {

        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[]{0x125, 0x235, 0x345, 0x404});

        long totalPredationDeaths = 0;
        long        initialSizePopulation;
        long        numOfFemalesDead;
        long        numOfMalesDead;
        long        numOfFemalesBorn;
        long        numOfMalesBorn;
        long        numBirths;
        long        numSurvivedMales;
        long        numSurvivedFemales;
        long    []  ageAtDeath = new long[15];
        long        totalFemalesDeath = 0;
        long        totalMalesDeath   = 0;

        double      numLitters;

        System.out.println("Computing simulation...");

        stats.setFemales(females);
        stats.setMales(males);

        for (int year = 0; year < simulationYear; year++) 
        {
            //System.out.println("Year " + (year + 1) + " computing...");

            initialSizePopulation = count(females) + count(males);

            numBirths            = 0;
            totalFemalesDeath    = 0;
            totalMalesDeath      = 0;
            totalPredationDeaths = 0;
            numOfFemalesBorn     = 0;
            numOfMalesBorn       = 0;
            numSurvivedMales     = 0;
            numSurvivedFemales   = 0;


            // Births
            for (int age = 0; age < 15; age++) 
            {
                if (age >= 1)
                {
                    if (females[age] > 0) 
                    {
                        if (females[age] > ESTIMATION_THRESHOLD) 
                        {
                            long[] estimatedNumBabies = estimateNumberOfBabies(females[age], males[age], rnd);

                            females[0] += estimatedNumBabies[0];
                            males  [0] += estimatedNumBabies[1];
                        } 
                        else 
                        {
                            for (int i = 0; i < females[age]; i++) 
                            {
                                numLitters = Rabbit.calculateNumberOfLitters(rnd);

                                for (int litter = 0; litter < numLitters; litter++) 
                                {
                                    int numBorn = rnd.nextInt(4) + 3; // (3 to 6 babies per litters)

                                    List<Rabbit> femaleBabies = Rabbit.giveBirth(numBorn, rnd);

                                    for (Rabbit rabbit : femaleBabies) 
                                    {
                                        if (rabbit.isFemale() && rabbit.isAlive()) 
                                        {
                                            females[0]++;
                                        } 
                                        else if (!rabbit.isFemale() && rabbit.isAlive()) 
                                        {
                                            males[0]++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ageAtDeath[age] = 0;
            }

            numOfFemalesBorn = females[0];
            numOfMalesBorn   = males  [0];
            numBirths        = numOfFemalesBorn + numOfMalesBorn;

            
            for (int age = 1; age < 15; age++) 
            {
                numOfFemalesDead = 0;
                numOfMalesDead = 0;

                // Males
                if (males[age] > 0) 
                {
                    numSurvivedMales = (long) (males[age] * Rabbit.calculateSurvivalRate(age));
                    numOfMalesDead   = males[age] - numSurvivedMales;
                    males[age]       = numSurvivedMales;
                }  

                // Females
                if (females[age] > 0) 
                {
                    numSurvivedFemales = (long) (females[age] * Rabbit.calculateSurvivalRate(age));
                    numOfFemalesDead   = females[age] - numSurvivedFemales;
                    females[age]       = numSurvivedFemales;
                }

                // To simulate predation 
                //long[] predation = simulatePredation(females, males, age, rnd);

                totalFemalesDeath += numOfFemalesDead; //+ predation[0];
                totalMalesDeath   += numOfMalesDead;   //+ predation[1];

                // totalPredationDeaths += predation[0] + predation[1];
                ageAtDeath[age] =  age * (numOfMalesDead + numOfFemalesDead);
            }


            stats.updateStats(initialSizePopulation, females, males, numBirths, numOfFemalesBorn, numOfMalesBorn,
                    totalFemalesDeath, totalMalesDeath, ageAtDeath);

            stats.displayStats(year);

            //System.out.println("Year " + (year + 1) + " : Total deaths by predation:\t " + totalPredationDeaths);

            // Increage age for each age
            for (int age = 14; age >= 1; age--) 
            {
                females[age] = females[age - 1];
                males[age]   = males[age - 1];
            } 

            females[0] = 0;
            males  [0] = 0;
        }
    }



     /**
     * Simulates predation based on pre-defined probabilities.
     *
     * @param females the array representing the population of females at different age levels
     * @param males   the array representing the population of males at different age levels
     * @param rnd     the random number generator
     */

    public long[] simulatePredation(long[] females, long[] males, int age, MTRandom rnd) 
    {
        // Probabilité de prédation pour chaque tranche d'âge
        double predationProbability = CONSTANT_PREDATION_PROBABILITIES[age - 1];

        // Prédation sur les femelles
        long numFemalesPredated = (long) (females[age] * (rnd.nextDouble() * predationProbability));
        females[age] -= numFemalesPredated;

        // Prédation sur les mâles
        long numMalesPredated = (long) (males[age] * (rnd.nextDouble() * predationProbability));
        males[age]   -= numMalesPredated;

        /*System.out.println("--- Predateurs ---");
        System.out.println(" Age : " + age);
        System.out.println("Females : " + numFemalesPredated);
        System.out.println("Males : " + numMalesPredated);
        System.out.println("========================================");*/

        return new long[]{numFemalesPredated, numMalesPredated};
    }

    /**
     * Counts the total population at all age levels.
     *
     * @param population the array representing the population at different age levels
     * @return           the total population
     */

    public long count(long[] population) 
    {
        long total = 0;
        for (int i = 0; i < 15; i++) {
            total += population[i];
        }
        return total;
    }

 
     /**
     * Displays the population of rabbits at different age levels.
     *
     * @param population the array representing the population at different age levels
     * @param name       the name of the population (e.g., "Females" or "Males")
     * @param n          the number of age levels to display
     */

    public void displayPopulation(long[] population, String name, int n) 
    {
        System.out.println(name);

        for (int i = 0; i < n; i++) 
        {
            System.out.println("[" + i + "] : " + population[i] + " ");
        }

        System.out.println();
    }
}
