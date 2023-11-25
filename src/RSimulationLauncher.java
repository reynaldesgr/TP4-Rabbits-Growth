/**
 * The RSimulationLauncher class initiates and manages multiple rabbit population simulations.
 * It runs a specified number of simulations, collects and averages the results, and prints
 * statistics such as the average number of females, males, deaths, births, and population size.
 * 
 * @author SEGERIE Reynalde
 */

package src;

import java.io.IOException;

public class RSimulationLauncher 
{
    private static final int NUM_SIMULATIONS = 1;
    private static final int NUM_FEMALES     = 10;
    private static final int NUM_MALES       = 10;
    private static final int NUM_YEARS       = 15;

    /**
     * Launches multiple rabbit population simulations and prints average statistics.
     * @throws IOException
     */

    public void launchSimulations() throws IOException
    {   

        double averageFemalesPerSimulations               = 0;
        double averageMalesPerSimulations                 = 0;
        double averageDeathsPerSimulations                = 0;
        double averagePopulationSizePerSimulations        = 0;
        double averageBirthsPerSimulations                = 0;

        double averagePercentFemalesPerSimulations        = 0.;
        double averagePercentMalesPerSimulations          = 0.;

        /**
         * The statistics object to collect data from each simulation.
         */

        RSimulationStats stats              = new RSimulationStats();

        System.out.println("FEMALES : " + NUM_FEMALES + " | MALES : " + NUM_MALES);
        for (int i = 0; i < NUM_SIMULATIONS; i++)
        {
            System.out.println("\t SIMU. " + (i + 1) + " : \t");

            RabbitSimulation simulation = new RabbitSimulation(NUM_FEMALES, NUM_MALES, NUM_YEARS);
            
            simulation.runSimulation(stats);

            averageFemalesPerSimulations               += stats.getFemales();
            averageMalesPerSimulations                 += stats.getMales();
            averageDeathsPerSimulations                += stats.getFemalesDead() + stats.getMalesDead();
            averagePopulationSizePerSimulations        += stats.getTotalPopulation();
            averageBirthsPerSimulations                += stats.getBirths();
            averagePercentFemalesPerSimulations        += stats.getPercentageFemales();
            averagePercentMalesPerSimulations          += stats.getPercentageMales();

        }

        // Calculate averages
        averageFemalesPerSimulations              /= NUM_SIMULATIONS;
        averageMalesPerSimulations                /= NUM_SIMULATIONS;
        averageDeathsPerSimulations               /= NUM_SIMULATIONS;
        averagePopulationSizePerSimulations       /= NUM_SIMULATIONS;
        averageBirthsPerSimulations               /= NUM_SIMULATIONS;

        averagePercentFemalesPerSimulations       /= NUM_SIMULATIONS;
        averagePercentMalesPerSimulations         /= NUM_SIMULATIONS;
        


        System.out.println("Average females per simulations:\t"           + averageFemalesPerSimulations);
        System.out.println("Average males per simulations:\t"             + averageMalesPerSimulations);
        System.out.println("Average deaths per simulations:\t"            + averageDeathsPerSimulations);
        System.out.println("Average population size per simulations:\t"   + averagePopulationSizePerSimulations);
        
        System.out.println("Average births per simulations: \t"           + averageBirthsPerSimulations);
        System.out.println("Average % females per simulations.:\t"        + averagePercentFemalesPerSimulations + "% ");
        System.out.println("Average % males per simulations.:\t"          + averagePercentMalesPerSimulations + "% ");

        System.out.println("========================================================================");
    }
}
