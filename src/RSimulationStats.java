package src;

public class RSimulationStats 
{
    private long   initialSizePopulation;

    private long[] females;
    private long[] males;

    private long   femalesDead;
    private long   malesDead;

    private long   femalesBorn;
    private long   malesBorn;

    private int    totalAgeAtDeath;

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

        this.totalAgeAtDeath       = 0;
    }


    public void updateStats(long initialSizePopulation, long[] females, long[] males, long births, long femalesBorn, long malesBorn, long femalesDead, long malesDead, int totalAgeAtDeath)
    {
        this.initialSizePopulation = initialSizePopulation;

        this.females               = females;
        this.males                 = males;

        this.births                = births;

        this.femalesDead           = femalesDead;
        this.malesDead             = malesDead;

        this.femalesBorn           = femalesBorn;
        this.malesBorn             = malesBorn;

        this.totalAgeAtDeath       = totalAgeAtDeath;

    }

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
        System.out.println("Average age : "                  + Math.round(calculateAverageAge()));
        if (femalesDead != 0 || malesDead != 0)
        {
            System.out.println("Average age at death : "        + Math.round(totalAgeAtDeath / (femalesDead + malesDead)));
        }
        System.out.println("===============================================");
        System.out.println("Total Population : "                + getTotalPopulation());
        System.out.println("Total Females :\t"                  + count(females));
        System.out.println("Total Males :\t"                    + count(males));
        System.out.println("===============================================");
        System.out.println();
    }

    private double calculatePercentage(long count1, long count2) 
    {
        if (count1 + count2 == 0) 
        {
            return 0.0;
        }
        return (double) count1 / (count1 + count2) * 100.0;
    }

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
            sum += females[i] * (i + 1) + males[i] * (i + 1); // Contribution de chaque groupe d'âge
        }

        return (double) sum / totalPopulation;
    }

    public double calculatePopulationGrowthRate(long previousPopulation, long currentPopulation) 
    {
        if (previousPopulation == 0) 
        {
            return 0.0;
        }

        return ((double) currentPopulation - previousPopulation) / previousPopulation * 100.0;
    }


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

    public long getTotalPopulation() 
    {
        return count(females) + count(males);
    } 

    public long getFemalesDead()
    {
        return femalesDead;
    }

    public long getMalesDead()
    {
        return malesDead;
    }

    public long getMales()
    {
        return count(males);
    }

    public long getFemales()
    {
        return count(females);
    }

    public long getBirths()
    {
        return births;
    }


    public double getPercentageFemales()
    {
       return Math.round(calculatePercentage(count(females), count(males)));
    }

    public double getPercentageMales()
    {
       return Math.round(calculatePercentage(count(males), count(females)));
    }

    public void setFemales(long[] females)
    {
        this.females = females;
    }

    public void setMales(long[] males)
    {
        this.males = males;
    }
}
