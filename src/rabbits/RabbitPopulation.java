package src.rabbits;

import java.util.ArrayList;
import java.util.List;

public class RabbitPopulation 
{
    private int numberOfCouples;
    private int timeStep;

    private List<Rabbit> rabbits;

    public RabbitPopulation(int numberOfCouples)
    {
        this.timeStep = 0;

        rabbits = new ArrayList<Rabbit>();
    }

    public int getTimeStep() 
    {
        return timeStep;
    }

}
