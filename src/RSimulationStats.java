/**
 * The RSimulationStats class represents statistics for a rabbit population simulation.
 * It includes information about the initial and final population, births, deaths,
 * average age, and other relevant statistics.
 * 
 * @author SEGERIE Reynalde
 */

package src;

public class RSimulationStats 
{
    private long   initialSizePopulation;

    private long[] females;
    private long[] males;

    private long[] ageAtDeath;

    private long   femalesDead;
    private long   malesDead;

    private long   femalesBorn;
    private long   malesBorn;


    private long   births;

    public RSimulationStats(){}

    public RSimulationStats(long[] females, long[] males) 
    {
        this.initialSizePopulation = count(females) + count(males);

        this.females               = females;
        this.males                 = males;
        
        this.births                = 0;
        
        this.femalesDead           = 0;
        this.malesDead             = 0;

        this.femalesBorn           = 0;
        this.malesBorn             = 0;
    }


    /**
     * Updates the statistics with new information from the simulation.
     *
     * @param initialSizePopulation the initial size of the population
     * @param females               the array representing the population of females at different age levels
     * @param males                 the array representing the population of males at different age levels
     * @param births                the number of births in the current simulation year
     * @param femalesBorn           the number of female rabbits born in the current simulation year
     * @param malesBorn             the number of male rabbits born in the current simulation year
     * @param femalesDead           the number of female rabbits that died in the current simulation year
     * @param malesDead             the number of male rabbits that died in the current simulation year
     * @param ageAtDeath            the array representing the age at which rabbits died
     */

    public void updateStats(long initialSizePopulation, long[] females, long[] males, long births, long femalesBorn, long malesBorn, long femalesDead, long malesDead, long[] ageAtDeath)
    {
        this.initialSizePopulation = initialSizePopulation;

        this.females               = females;
        this.males                 = males;

        this.births                = births;

        this.femalesDead           = femalesDead;
        this.malesDead             = malesDead;

        this.femalesBorn           = femalesBorn;
        this.malesBorn             = malesBorn;

        this.ageAtDeath            = ageAtDeath;
    }

    /**
     * Displays the statistics for a specific simulation year.
     *
     * @param year the simulation year
     */

    public void displayStats(int year) 
    {
        System.out.println();
        System.out.println("-- Year " + (year + 1) + " stats : --");
        System.out.println("\n* Initial size at the beginning of the year : " + this.initialSizePopulation);
        System.out.println("Percentage of Females: "         + Math.round(calculatePercentage(count(females), count(males))) + "%");
        System.out.println("Percentage of Males: "           + Math.round(calculatePercentage(count(males), count(females))) + "%");
        System.out.println("Number of births : "             + births);
        System.out.println("Number of deaths : "             + (femalesDead + malesDead) );
        System.out.println("Number of females deaths : "     + femalesDead);
        System.out.println("Number of males deaths : "       + malesDead);
        System.out.println("Number of females born : "       + femalesBorn);
        System.out.println("Number of males born : "         + malesBorn);
        System.out.printf("Average age : %.2f \n",    calculateAverageAge());
        if (femalesDead != 0 || malesDead != 0)
        {
            System.out.printf("Average age at death : %.2f \n",   calculateWeightedAverage(ageAtDeath));
        }
        System.out.println("-------------------------------------------------");
        System.out.printf("Age Coefficient of variation: %.2f\n", calculateAgeCoefficientOfVariation());
        System.out.printf("Life expectancy: %.2f\n",              calculateLifeExpectancy());
        System.out.printf("Females to males ratio: %.2f\n",       calculateFemalesToMalesRatio());
        System.out.println("-------------------------------------------------");

        displayAgeDistribution();

        System.out.println("===============================================");
        System.out.println("Total Population : "                        + getTotalPopulation());
        System.out.println("Total Females :\t"                          + count(females));
        System.out.println("Total Males :\t"                            + count(males));
        System.out.println("===============================================");
        System.out.println();
    }

    /**
     * Calculates the weighted average age at death based on the ages and the number of rabbits that died at each age.
     *
     * @param ageAtDeath the array representing the age at which rabbits died
     * @return           the weighted average age at death
     */

    private static double calculateWeightedAverage(long[] ageAtDeath) 
    {
        long totalDecedents = 0;
        long sumWeightedAges = 0;

        for (int age = 0; age < ageAtDeath.length; age++) 
        {
            totalDecedents  += ageAtDeath[age];
            sumWeightedAges += age * ageAtDeath[age];
        }

        if (totalDecedents == 0) {
            return 0;
        }

        return (double) sumWeightedAges / totalDecedents;
    }

    /**
     * Calculates the percentage of one count relative to the sum of two counts.
     *
     * @param count1 the first count
     * @param count2 the second count
     * @return the percentage of count1 relative to the sum of count1 and count2
     */

    private double calculatePercentage(long count1, long count2) 
    {
        if (count1 + count2 == 0) 
        {
            return 0.0;
        }
        return (double) count1 / (count1 + count2) * 100.0;
    }


    /**
     * Calculates the average age of the rabbit population.
     *
     * @return the average age of the population
     */

    public double calculateAverageAge() 
    {
        long totalPopulation = getTotalPopulation();
        if (totalPopulation == 0) 
        {
            return 0.0;
        }

        long sum = 0;
        for (int i = 0; i < females.length; i++) 
        {
            sum += females[i] * (i + 1) + males[i] * (i + 1); 
        }

        return (double) sum / totalPopulation;
    }


    public double calculateAgeCoefficientOfVariation() 
    {
        double averageAge    = calculateAverageAge();
        long totalPopulation = getTotalPopulation();
    
        if (totalPopulation <= 1) 
        {
            return 0.0; // No variations if population has 1 or 0 individuals
        }
    
        double sumSquaredDeviations = 0;
    
        for (int i = 0; i < females.length; i++) 
        {
            sumSquaredDeviations += Math.pow(i + 1 - averageAge, 2) * (females[i] + males[i]);
        }
    
        return Math.sqrt(sumSquaredDeviations / totalPopulation) / averageAge;
    }
    

    /**
     * Calculates the population growth rate between two populations.
     *
     * @param previousPopulation the population size in the previous year
     * @param currentPopulation  the population size in the current year
     * @return                   the population growth rate
     */

    public double calculatePopulationGrowthRate(long previousPopulation, long currentPopulation) 
    {
        if (previousPopulation == 0) 
        {
            return 0.0;
        }

        return ((double) currentPopulation - previousPopulation) / previousPopulation * 100.0;
    }


    public double calculateFemalesToMalesRatio() 
    {
        long totalFemales = count(females);
        long totalMales   = count(males);
    
        if (totalMales == 0) {
            return totalFemales; 
        }
    
        return (double) totalFemales / totalMales;
    }

    
    public double calculateLifeExpectancy() 
    {
        long totalDeaths = getFemalesDead() + getMalesDead();
        if (totalDeaths == 0) 
        {
            return 0.0;
        }
    
        long totalAgeAtDeath = 0;
        for (int i = 0; i < ageAtDeath.length; i++) 
        {
            totalAgeAtDeath += i * ageAtDeath[i];
        }
    
        return (double) totalAgeAtDeath / totalDeaths;
    }
    

    public void displayAgeDistribution() 
    {
        System.out.println("\nAge Distribution of Living Rabbits:");
        for (int i = 0; i < females.length; i++) 
        {
            System.out.println("Age " + (i + 1) + ":\t " + (females[i] + males[i]));
        }
    }
    

    /**
     * Counts the total number of rabbits in the given population array.
     *
     * @param population the array representing the population of rabbits
     * @return the total number of rabbits in the population array
     */

    private long count(long[] population) 
    {
        long total = 0;
        if (population != null)
        {
            for (long count : population) 
            {
                total += count;
            }
        }
        return total;
    }

    /**
     * Calculates the percentage of females in the population.
     *
     * @return the percentage of females
     */

    public double getPercentageFemales()
    {
       return Math.round(calculatePercentage(count(females), count(males)));
    }

    /**
     * Calculates the percentage of males in the population.
     *
     * @return the percentage of males
     */
    
    public double getPercentageMales()
    {
       return Math.round(calculatePercentage(count(males), count(females)));
    }


    /**
     * Gets the total population by summing the number of females and males.
     *
     * @return the total population
     */

    public long getTotalPopulation() 
    {
        return count(females) + count(males);
    } 

    /**
     * Gets the number of dead females.
     *
     * @return the number of dead females
     */

    public long getFemalesDead()
    {
        return femalesDead;
    }

    /**
     * Gets the number of dead males.
     *
     * @return the number of dead males
     */

    public long getMalesDead()
    {
        return malesDead;
    }

    /**
     * Gets the total number of males.
     *
     * @return the total number of males
     */

    public long getMales()
    {
        return count(males);
    }

    /**
     * Sets the population of males.
     *
     * @param males the array representing the population of males
     */

    public void setMales(long[] males)
    {
        this.males = males;
    }

    /**
     * Gets the total number of females.
     *
     * @return the total number of females
     */

    public long getFemales()
    {
        return count(females);
    }

    /**
     * Sets the population of females.
     *
     * @param females the array representing the population of females
     */

    public void setFemales(long[] females)
    {
        this.females = females;
    }

    /**
     * Gets the number of births.
     *
     * @return the number of births
     */

    public long getBirths()
    {
        return births;
    }

}
