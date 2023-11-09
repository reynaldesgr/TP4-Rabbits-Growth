package src;

public class RSimulationStats 
{
    private int    yearsSimulated;

    private long   totalFemales;
    private long   totalMales;
    private long   totalRabbits;
    private long   totalBirths;
    private long   totalDeaths;
    private long   totalYoungRabbits;
    private long   totalPredators;
    private double totalAges; 

    public RSimulationStats(int years) 
    {
        // Initialize statistics
        this.totalFemales      = 0;
        this.totalMales        = 0;
        this.totalRabbits      = 0;
        this.totalBirths       = 0;
        this.totalDeaths       = 0;
        this.totalYoungRabbits = 0;
        this.totalPredators    = 0;
        this.totalAges         = 0;

        this.yearsSimulated = years;
    }

    /**
     * Update statistics based on the current population.
     * @param females Number of females in the last year
     * @param males Number of males in the last year
     * @param predators Number of predators in the last year
     * @param births Number of births in the last year
     * @param deaths Number of deaths in the last year
     * @param ages Sum of ages of all rabbits
     */
    public void updateStats(long females, long males, long deaths, long births, double ages) 
    {
        // Update statistics by iterating through the population
        totalFemales      += females;
        totalMales        += males;
        totalBirths       += births;
        totalYoungRabbits += females + males;
        totalAges         += ages;
        this.totalDeaths   = deaths;

        totalRabbits = totalFemales + totalMales;
    }

    /**
     * Calculate the percentage of females in the population.
     * @return The percentage of females
     */
    public double calculatePercentageFemales() 
    {
        if (totalRabbits == 0) {
            return 0.0;
        }
        return (double) totalFemales / totalRabbits * 100;
    }

    /**
     * Calculate the percentage of males in the population.
     * @return The percentage of males
     */
    public double calculatePercentageMales() 
    {
        if (totalRabbits == 0) {
            return 0.0;
        }
        return (double) totalMales / totalRabbits * 100;
    }

    /**
     * Calculate the percentage of predators in the population.
     * @return The percentage of predators
     */
    public double calculatePercentagePredators() 
    {
        if (totalRabbits == 0) {
            return 0.0;
        }
        return (double) totalPredators / totalRabbits * 100;
    }

    /**
     * Calculate the average number of births per year.
     * @return The average number of births per year
     */
    public double calculateAverageBirths() 
    {
        if (yearsSimulated == 0) {
            return 0.0;
        }
        return (double) totalBirths / yearsSimulated;
    }

    /**
     * Calculate the average number of deaths per year.
     * @return The average number of deaths per year
     */
    public double calculateAverageDeaths() 
    {
        if (yearsSimulated == 0) {
            return 0.0;
        }
        return (double) totalDeaths / yearsSimulated;
    }

    /**
     * Calculate the average age of the rabbits in the population.
     * @return The average age of rabbits
     */
    public double calculateAverageAge() 
    {
        if (totalRabbits == 0) {
            return 0.0;
        }
        return (double) totalAges / (double) totalRabbits;
    }

    /**
     * Get the total number of females.
     * @return The total number of females
     */
    public long getTotalFemales() 
    {
        return totalFemales;
    }

    /**
     * Get the total number of males.
     * @return The total number of males
     */
    public long getTotalMales() 
    {
        return totalMales;
    }

    /**
     * Get the total number of rabbits.
     * @return The total number of rabbits
     */
    public long getTotalRabbits() 
    {
        return totalRabbits;
    }

    /**
     * Get the total number of births.
     * @return The total number of births
     */
    public long getTotalBirths() 
    {
        return totalBirths;
    }

    /**
     * Get the total number of deaths.
     * @return The total number of deaths
     */
    public long getTotalDeaths() 
    {
        return totalDeaths;
    }

    /**
     * Get the total number of young rabbits (age < 1 year).
     * @return The total number of young rabbits
     */
    public long getTotalYoungRabbits() 
    {
        return totalYoungRabbits;
    }


    public void printStats() 
    {
        System.out.println("Total Females: " + getTotalFemales());
        System.out.println("Total Males: " + getTotalMales());
        System.out.println("Total Rabbits: " + getTotalRabbits());
        System.out.println("Total Births: " + getTotalBirths());
        System.out.println("Total Deaths: " + getTotalDeaths());
        System.out.println("Total Young Rabbits: " + getTotalYoungRabbits());
        System.out.println("Percentage Females: " + calculatePercentageFemales() + "%");
        System.out.println("Percentage Males: " + calculatePercentageMales() + "%");
        System.out.println("Average Births Per Year: " + calculateAverageBirths());
        System.out.println("Average Deaths Per Year: " + calculateAverageDeaths());
        System.out.println("Average Age of Rabbits: " + calculateAverageAge());
    }

}
