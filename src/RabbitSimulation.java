package src;

import src.rabbits.Rabbit;

import java.util.List;

public class RabbitSimulation 
{
    private long[] females = new long[15];
    private long[] males   = new long[15];
    private int simulationYear;

    private static final long ESTIMATION_THRESHOLD = 1000000;

    public RabbitSimulation(int initialFemales, int initialMales, int simulationYear) 
    {
        this.simulationYear = simulationYear;

        // Mature rabbits
        females[1] = initialFemales;
        males  [1] = initialMales;
    }

    private long[] estimateNumberOfBabies(long numMatureFemales, long numMatureMales, MTRandom rnd) 
    {
        // Facteur de base pour estimer le nombre de lapereaux par lapine mature
        double baseFactor = 4.8;
    
        // Facteur supplémentaire en fonction de la disponibilité des mâles
        double maleAvailabilityFactor = Math.min(1.0, (double) numMatureMales / numMatureFemales);

        double randomFactor = 0.4 + rnd.nextDouble() * 0.08;

        // Estimation finale en utilisant les facteurs
        double estimatedNumBabies = (baseFactor * maleAvailabilityFactor * randomFactor) * numMatureFemales;
    
        // Estimer le nombre de naissances pour les mâles et les femelles
        long estimatedNumFemales = (long) (estimatedNumBabies * 0.5);  //50% de femelles
        long estimatedNumMales   = (long) (estimatedNumBabies * 0.5); // 50% de mâles
    
        estimatedNumFemales = Math.max(0, estimatedNumFemales);
        estimatedNumMales   = Math.max(0, estimatedNumMales);
    
        // Retourne un tableau avec les estimations pour les mâles et les femelles
        return new long[]{estimatedNumFemales, estimatedNumMales};
    }
    

    public void runSimulation(RSimulationStats stats) 
    {

        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[]{0x125, 0x235, 0x345, 0x404});

        double      numLitters;
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
        long        totalMalesDeath = 0;

        System.out.println("Computing simulation...");

        stats.setFemales(females);
        stats.setMales(males);

        for (int year = 0; year < simulationYear; year++) 
        {
            //System.out.println("Year " + (year + 1) + " computing...");

            initialSizePopulation = count(females) + count(males);

            numBirths          = 0;
            totalFemalesDeath  = 0;
            totalMalesDeath    = 0;
            numOfFemalesBorn   = 0;
            numOfMalesBorn     = 0;
            numSurvivedMales   = 0;
            numSurvivedFemales = 0;

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
                                    int numBorn = rnd.nextInt(5) + 4; // (4 à 8)

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

                totalFemalesDeath += numOfFemalesDead;
                totalMalesDeath   += numOfMalesDead;

                ageAtDeath[age] = age * (numOfMalesDead + numOfFemalesDead);
            }


            stats.updateStats(initialSizePopulation, females, males, numBirths, numOfFemalesBorn, numOfMalesBorn,
                    totalFemalesDeath, totalMalesDeath, ageAtDeath);

            stats.displayStats(year);

            for (int age = 14; age >= 1; age--) 
            {
                females[age] = females[age - 1];
                males[age] = males[age - 1];
            }

            females[0] = 0;
            males[0]   = 0;
        }
    }

    public long count(long[] population) 
    {
        long total = 0;
        for (int i = 0; i < 15; i++) {
            total += population[i];
        }
        return total;
    }

 
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
