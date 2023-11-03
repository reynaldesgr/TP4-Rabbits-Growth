package src;

public class RSimulationStats {
    private int totalBirths;
    private int totalDeaths;
    private int totalShotsByPredators;
    private int totalPregnancy;
    private int totalFemales;
    private int totalMales;
    private int initialPopulationSize;
    private int finalPopulationSize;


    public RSimulationStats(int totalBirths, int totalDeaths, int totalShotsByPredators, int totalPregnancy, int totalFemales, int totalMales, int initialPopulationSize, int finalPopulationSize) {
        this.totalBirths           = totalBirths;
        this.totalDeaths           = totalDeaths;
        this.totalShotsByPredators = totalShotsByPredators;
        this.totalPregnancy        = totalPregnancy;
        this.totalFemales          = totalFemales;
        this.totalMales            = totalMales;

        this.initialPopulationSize = initialPopulationSize;
        this.finalPopulationSize   = finalPopulationSize;
    }

    public int getTotalBirths() {
        return totalBirths;
    }
    
    public void setTotalBirths(int totalBirths) {
        this.totalBirths = totalBirths;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void setTotalDeaths(int totalDeaths) {
        this.totalDeaths = totalDeaths;
    }

    public int getTotalShotsByPredators() {
        return totalShotsByPredators;
    }

    public void setTotalShotsByPredators(int totalShotsByPredators) {
        this.totalShotsByPredators = totalShotsByPredators;
    }

    public int getTotalPregnancy()
    {
        return totalPregnancy;
    }

    public void setTotalPregnancy(int totalPregnancy)
    {
        this.totalPregnancy = totalPregnancy;
    }

    public int getTotalFemales()
    {
        return totalFemales;
    }

    public void setTotalFemales(int totalFemales)
    {
        this.totalFemales = totalFemales;
    }


    public int getTotalMales()
    {
        return totalMales;
    }

    public void setTotalMales(int totalMales)
    {
        this.totalMales = totalMales;
    }

    public int getFinalPopulationSize() {
        return finalPopulationSize;
    }

    public void setFinalPopulationSize(int finalPopulationSize) {
        this.finalPopulationSize = finalPopulationSize;
    }

    public double getAverageBirths(int numSimulations) {
        return (double) totalBirths / numSimulations;
    }

    public double getAverageDeaths(int numSimulations) {
        return (double) totalDeaths / numSimulations;
    }

    public double getAverageShotsByPredators(int numSimulations) {
        return (double) totalShotsByPredators / numSimulations;
    }

    public double getPregnancy(int numSimulations) 
    {
        return (double) totalPregnancy / numSimulations;
    }

    public double getBirthPercentage() {
        return (double) (totalBirths / (totalBirths + totalDeaths)) * 100;
    }

    public double getDeathPercentage() {
        return (Math.abs((double) totalDeaths - (double) totalShotsByPredators) / ((double) totalDeaths)) * 100;
    }

    public double getShotsPercentage() {
        return ((double) totalShotsByPredators / ((double) totalDeaths)) * 100;
    }

    public double getFemalePercentage()
    {
        return (double) totalFemales / finalPopulationSize * 100;
    }

    public double getMalePercentage()
    {
        return (double) totalMales / finalPopulationSize * 100;
    }


    public void display(int simulationMonths)
    {
        System.out.println(" -- Simulation Complete -- ");
        System.out.println("* Total Births: "     + getTotalBirths());
        System.out.println("* Total Deaths: "     + getTotalDeaths());
        System.out.println("* Average Births: "   + String.format("%.2f%%", getAverageBirths(simulationMonths)));
        System.out.println("* Average Deaths: "   + String.format("%.2f%%", getAverageDeaths(simulationMonths)));
        System.out.println("* Death by Predator - Percentage: "     + String.format("%.2f%%", getShotsPercentage()));
        System.out.println("* Natural death     - Percentage: "     + String.format("%.2f%%", getDeathPercentage()));
    }
}
