package src.rabbits;

import java.util.ArrayList;
import java.util.List;

import src.MTRandom;

public class Rabbit {
    private static int rabbitCount = 0;

    private int id;
    private int ageInMonths; // Age in months
    private boolean isPregnant;
    private Sex sex;

    private double survivalRate;
    
    public Rabbit(int ageInMonths, Sex sex) 
    {
        this.id = ++rabbitCount;

        this.ageInMonths = ageInMonths;
        this.isPregnant = false;
        this.sex = sex;

        this.survivalRate = calculateSurvivalRate(ageInMonths);
    }

    public void incrementAge() 
    {
        this.ageInMonths++;
        survivalRate = calculateSurvivalRate(ageInMonths);
    }

    public boolean canGiveBirth() 
    {
        return sex == Sex.FEMALE && ageInMonths >= 5;
    }

    public boolean isAlive(MTRandom random) 
    {
       return random.nextDouble() <= survivalRate;
    }

    public boolean isPregnant() 
    {
        return isPregnant;
    }

    public void gettingPregnant() 
    {
        if (canGiveBirth() && !isPregnant) 
        {
            isPregnant = true;
        }
    }

    public List<Rabbit> giveBirth(MTRandom random) 
    {
        double probabilityOfLitters[] = { 0.1, 0.4, 0.3, 0.2 };
        double rg = random.nextDouble();

        int numberOfLitters;
        int numberOfKittens;

        List<Rabbit> newKittens = new ArrayList<>();

        if (rg < probabilityOfLitters[0]) 
        {
            numberOfLitters = 4;
        } else if (rg < probabilityOfLitters[0] + probabilityOfLitters[1]) 
        {
            numberOfLitters = 5;
        } else if (rg < probabilityOfLitters[0] + probabilityOfLitters[1] + probabilityOfLitters[2]) 
        {
            numberOfLitters = 6;
        } else 
        {
            numberOfLitters = 7;
        }

        for (int i = 0; i < numberOfLitters; i++) 
        {
            numberOfKittens = 3 + random.nextInt(4); // Equal chance to have 3 to 6 kittens per litter

            for (int j = 0; j < numberOfKittens; j++) 
            {
                Sex kittenSex = determineSexOfKitten(random);

                Rabbit kitten = new Rabbit(0, kittenSex);
                newKittens.add(kitten);
            }
        }

        return newKittens;
    }

    public static Sex determineSexOfKitten(MTRandom random) {
        double naturalProbability = 0.5;
        double rg = random.nextDouble();

        if (rg < naturalProbability) 
        {
            return Sex.MALE;
        } else 
        {
            return Sex.FEMALE;
        }
    }

    private double calculateSurvivalRate(int ageInMonths) {
        double survivalRate = 0.0;

        if (ageInMonths < 5) 
        {
            // Survival rate for little rabbits (35%)
            survivalRate = 0.35;
            // Sexuality maturity is reached between 5 to 8 months after the birth of kittens

        } 
        else if (ageInMonths >= 5 && ageInMonths < 120) 
        {
            // Survival rate for adult rabbits (60%)
            survivalRate = 0.60;

        } 
        else if (ageInMonths >= 120 && ageInMonths < 180) 
        {
            // Diminish survival rate by 10% each month after age 10
            survivalRate = 0.60 - ((ageInMonths - 120) * 0.10 / 12.0);
            
        } 
        else 
        {
            // Survival rate is 0% for rabbits older than 180 months (15 years)
            survivalRate = 0.0;
        }

        return survivalRate;
    }

    @Override
    public String toString() 
    {
        String displayMsg = "[Rabbit]\n" + "\nNum. : " + this.id + "\nAge (months) : " + this.ageInMonths
                + "\nSexe : " + this.getSex();

        if (this.sex.equals(Sex.FEMALE)) 
        {
            displayMsg += "\nPregnant : " + this.sex;
        }

        return displayMsg;
    }

    public static int getRabbitCount() 
    {
        return rabbitCount;
    }

    public static void setRabbitCount(int rabbitCount) 
    {
        Rabbit.rabbitCount = rabbitCount;
    }

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public int getAgeInMonths() 
    {
        return ageInMonths;
    }

    public void setAgeInMonths(int ageInMonths) 
    {
        this.ageInMonths = ageInMonths;
    }

    public void setPregnant(boolean isPregnant) 
    {
        this.isPregnant = isPregnant;
    }

    public Sex getSex() 
    {
        return sex;
    }

    public void setSex(Sex sex) 
    {
        this.sex = sex;
    }
}
