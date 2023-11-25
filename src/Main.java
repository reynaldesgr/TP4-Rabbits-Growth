/**
 * The Main class contains the main method for executing a simple rabbit population simulation.
 * It includes simulations based on the Fibonacci sequence to represent rabbit pairs' growth over months.
 * Additionally, it uses a more realistic population simulation with the help of the RSimulationLauncher.
 * 
 * @author SEGERIE Reynalde
 */

package src;

import java.io.IOException;

public class Main 
{

    /**
     * The main method of the program, responsible for executing the rabbit population simulations.
     *
     * @param args the command-line arguments (not used in this program)
     * @throws IOException
     */
    
    public static void main(String[] args) throws IOException 
    {
        // Q1.

        // Number of months to simulate        
        int months = 12; 

        // Population
        int[] population = new int[months];

        // Start with 1 pair of rabbits (1 adult pair) - (Generation)
        population[0] = 1;

        // The second month, there is still 1 pair of rabbits - (Maturation)
        population[1] = 1; 

        System.out.println("Month 1: 1 pair of rabbits (0 pair of adult)");
        System.out.println("Month 2: 1 pair of rabbits (1 pair of adult)");

        for (int i = 2; i < months; i++) 
        {
            population[i] = population[i - 1] + population[i - 2];
            System.out.println("Month " + (i + 1) + " \t:\t " + population[i] + "\t pairs of rabbits (" + population[i - 1] + " pair(s) of adults)");
        }

        // Q2.

        // More realistic simulation
        RSimulationLauncher launcher = new RSimulationLauncher();
        // Compute NB_SIMULATIONS 
        launcher.launchSimulations();
    }
}


