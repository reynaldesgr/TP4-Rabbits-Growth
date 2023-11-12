package src;

import src.rabbits.Rabbit;

import java.util.List;

public class RabbitSimulation {
    private long[] females = new long[15];
    private long[] males   = new long[15];
    private int simulationYear;

    public RabbitSimulation(int initialFemales, int initialMales, int simulationYear) 
    {
        this.simulationYear = simulationYear;
        females[0]          = initialFemales;
        males  [0]          = initialMales;
    }

    public long count(long[] population) 
    {
        long total = 0;
        for (int i = 0; i < 15; i++) 
        {
            total += population[i];
        }
        return total;
    }

    public void runSimulation(RSimulationStats stats) 
    {
        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[]{0x123, 0x231, 0x341, 0x404});

        long   numSurvived;
        double numLitters;

        long   initialSizePopulation;
        long   numOfFemalesDead;
        long   numOfMalesDead;

        long   numOfFemalesBorn;
        long   numOfMalesBorn;

        long   numBirths;
        int    ageAtDeath;

        System.out.println("Computing simulation...");

        stats.setFemales(females);
        stats.setMales(males);

        for (int year = 0; year < simulationYear; year++)
        {
            initialSizePopulation = count(females) + count(males);

            numBirths        = 0;
            numSurvived      = 0;

            numOfFemalesDead = 0;
            numOfMalesDead   = 0;

            numOfFemalesBorn = 0;
            numOfMalesBorn   = 0;

            ageAtDeath       = 0;

            // Males
            for (int age = 14; age >= 1; age--) 
            {
                if (males[age] > 0)
                {
                    
                    for (long n = 0; n <= males[age]; n++)
                    {
                        if (rnd.nextDouble() < Rabbit.calculateSurvivalRate(age))
                        {
                            numSurvived++;
                        }
                        else
                        {
                            numOfMalesDead++;
                            ageAtDeath+=age;
                        }
                    }
                    
                    males[age] = numSurvived;
                }
            }

            numSurvived    = 0;

            // Females
            for (int age = 14; age >= 1; age--)
            {
                // Increment age for existing females
                if (females[age] > 0)
                {
                    for (long n = 0; n <= females[age]; n++)
                    {
                        if (rnd.nextDouble() < Rabbit.calculateSurvivalRate(age))
                        {
                            numSurvived++;
                        }
                        else
                        {
                            numOfFemalesDead++;
                            ageAtDeath+=age;
                        }

                    }

                    females[age]          = numSurvived;

                    // Pregnancy
                    if (females[age] > 0 && age > 2)
                    {
                        for (int i = 0; i <= females[age]; i++)
                        {
                            // Generate the number of litters for each female (3 to 6)
                            numLitters = Rabbit.calculateNumberOfLitters(rnd);

                            // Add new babies for each litter
                            for (int litter = 0; litter < numLitters; litter++) 
                            {
                                int numBorn = rnd.nextInt(5) + 4; // (4 to 8)

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

            numOfFemalesBorn = females[0];
            numOfMalesBorn   = males[0];
            numBirths        = males[0] + females[0];

            for (int age = 14; age >= 1; age--)
            {
                females[age] = females[age - 1];
                males  [age] = males  [age - 1];
            }

            stats.updateStats(initialSizePopulation, females, males, numBirths, numOfFemalesBorn, numOfMalesBorn, numOfFemalesDead, numOfMalesDead, ageAtDeath);
            //if (year == 19) stats.displayStats(year);
        }
    }


}
