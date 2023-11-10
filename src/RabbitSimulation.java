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

    public void runSimulation() 
    {
        MTRandom rnd = new MTRandom();
        rnd.setSeed(new int[]{0x123, 0x231, 0x341, 0x404});

        long numSurvived;

        System.out.println("Females : " + count(females));
        System.out.println("Males : "   + count(males));

        for (int year = 0; year < simulationYear; year++)
        {
            numSurvived = 0;
            // Males
            for (int age = 14; age >= 1; age--) 
            {
                // Increment age for existing males
                males[age] = males[age - 1];

                if (males[age] > 0)
                {
                    
                    for (long n = 0; n <= males[age]; n++)
                    {
                        if (rnd.nextDouble() < Rabbit.calculateSurvivalRate(age))
                        {
                            numSurvived++;
                        }

                    }
                    males[age] = numSurvived;
                }
            }

            numSurvived = 0;

            // Females
            for (int age = 14; age >= 1; age--)
            {
                females[age] = females[age - 1];
                
                if (females[age] > 0)
                {
                    for (long n = 0; n <= females[age]; n++)
                    {
                        if (rnd.nextDouble() < Rabbit.calculateSurvivalRate(age))
                        {
                            numSurvived++;
                        }

                    }

                    females[age] = numSurvived;

                    if (females[age] > 0 && age > 2)
                    {
                        for (int i = 0; i <= females[age]; i++)
                        {
                            // Generate the number of litters for each female (3 to 6)
                            int    numLitters;
                            double rndNumber;

                            rndNumber = rnd.nextDouble();

                            if (rndNumber < 0.1)
                            {
                                numLitters = 3;
                            }
                            else if (rndNumber < 0.3)
                            {
                                numLitters = 4;
                            }
                            else if (rndNumber < 0.7)
                            {
                                numLitters = 5;
                            }
                            else
                            {
                                numLitters = 6;
                            }

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

            System.out.println("Year " + (year + 1) + ":");
            System.out.println("Females : " + count(females));
            System.out.println("Males : "   + count(males));
        }
    }
}
