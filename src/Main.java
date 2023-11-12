package src;


public class Main 
{

    public static void main(String[] args) 
    {
        /*int months = 12; // Number of months to simulate
        int[] population = new int[months];

        // Start with 1 pair of rabbits (1 adult pair) - (Generation)
        population[0] = 1;

        // The second month, there is still 1 pair of rabbits (Maturation)
        population[1] = 1; 

        System.out.println("Month 1: 1 pair of rabbits (0 adult)");
        System.out.println("Month 2: 1 pair of rabbits (1 adult)");

        for (int i = 2; i < months; i++) 
        {
            population[i] = population[i - 1] + population[i - 2];
            System.out.println("Month " + (i + 1) + " \t:\t " + population[i] + "\t pairs of rabbits (" + population[i - 1] + " adults)");
        }*/

        
        // More realistic simulation


        int initialAdult         = 1; // 2 rabbits, because it is a couple of adults
        int initialPredators     = 0;
        int simulationYear       = 20;  // Set the number of simulation months

        RabbitSimulation simulation = new RabbitSimulation(initialAdult, initialPredators, simulationYear);

        simulation.runSimulation();
    }
}


