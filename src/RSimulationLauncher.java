package src;

import java.util.Arrays;

public class RSimulationLauncher 
{
    private static final int NUM_SIMULATIONS = 1000;
    private static final int NUM_FEMALES     = 5;
    private static final int NUM_MALES       = 5;
    private static final int NUM_YEARS       = 20;

    private static final int BIN_SIZE            = 5000000;
    private static final int MAX_POPULATION_SIZE = 60000000;

    int numBins = MAX_POPULATION_SIZE / BIN_SIZE; // Number of bins

    public void launchSimulations()
    {   
        long[] populationCounts = new long[MAX_POPULATION_SIZE];

        double averageFemalesPerSimulations               = 0;
        double averageMalesPerSimulations                 = 0;
        double averageDeathsPerSimulations                = 0;
        double averagePopulationSizePerSimulations        = 0;
        double averageBirthsPerSimulations                = 0;

        double averagePercentFemalesPerSimulations        = 0.;
        double averagePercentMalesPerSimulations          = 0.;

        
        RSimulationStats stats              = new RSimulationStats();

        System.out.println("FEMALES : " + NUM_FEMALES + " | MALES : " + NUM_MALES);
        for (int i = 0; i < NUM_SIMULATIONS; i++)
        {
            System.out.println("\t SIMU. " + i + " : \t");

            RabbitSimulation simulation = new RabbitSimulation(NUM_FEMALES, NUM_MALES, NUM_YEARS);
            
            simulation.runSimulation(stats);

            averageFemalesPerSimulations               += stats.getFemales();
            averageMalesPerSimulations                 += stats.getMales();
            averageDeathsPerSimulations                += stats.getFemalesDead() + stats.getMalesDead();
            averagePopulationSizePerSimulations        += stats.getTotalPopulation();
            averageBirthsPerSimulations                += stats.getBirths();
            averagePercentFemalesPerSimulations        += stats.getPercentageFemales();
            averagePercentMalesPerSimulations          += stats.getPercentageMales();
            
            
            int totalPopulation = (int) stats.getTotalPopulation();
            //System.out.println(totalPopulation);

            // Ensure we don't go out of bounds
            if (totalPopulation < MAX_POPULATION_SIZE) {
                int binIndex = totalPopulation / BIN_SIZE;
                System.out.println("index : " + binIndex);
                populationCounts[binIndex]++;
            }

        }

        // Calcule les moyennes
        averageFemalesPerSimulations              /= NUM_SIMULATIONS;
        averageMalesPerSimulations                /= NUM_SIMULATIONS;
        averageDeathsPerSimulations               /= NUM_SIMULATIONS;
        averagePopulationSizePerSimulations       /= NUM_SIMULATIONS;
        averageBirthsPerSimulations               /= NUM_SIMULATIONS;

        averagePercentFemalesPerSimulations       /= NUM_SIMULATIONS;
        averagePercentMalesPerSimulations         /= NUM_SIMULATIONS;

        // Affiche les moyennes
        System.out.println("Average females per simulations :\t "           + averageFemalesPerSimulations);
        System.out.println("Average males per simulations :\t "             + averageMalesPerSimulations);
        System.out.println("Average deaths per simulations :\t "            + averageDeathsPerSimulations);
        System.out.println("Average population size per simulations :\t "   + averagePopulationSizePerSimulations);
        
        System.out.println("Average births per simulations : \t "           + averageBirthsPerSimulations);
        System.out.println("Average % females per simulations. :\t "        + averagePercentFemalesPerSimulations + "% ");
        System.out.println("Average % males per simulations. :\t "          + averagePercentMalesPerSimulations + "% ");

        displayHistogram(populationCounts);
    }

    public void displayHistogram(long[] populationCounts)
    {
        // Normalize the counts to get probabilities
        double[] probabilities = new double[MAX_POPULATION_SIZE];
        for (int i = 0; i < MAX_POPULATION_SIZE; i++) 
        {
            probabilities[i] = (double) populationCounts[i] / NUM_SIMULATIONS;
        }

        double maxProbability = 1;
        // Display the histogram
        System.out.println("\nHistogram:");
        for (int i = 0; i < numBins; i++) {
            int lowerBound = i * BIN_SIZE;
            int upperBound = (i + 1) * BIN_SIZE - 1;

            // Scale the bar based on the probability
            int scaledBarLength = (int) Math.round(probabilities[i] / maxProbability * 50);
            // Display the bar
            System.out.printf("[%d-%d]: %s%n", lowerBound, upperBound + 1, "*".repeat(scaledBarLength));
        }  
    }
}
