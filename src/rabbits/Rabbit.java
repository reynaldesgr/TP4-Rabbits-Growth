package src.rabbits;

import java.util.ArrayList;
import java.util.List;
import src.MTRandom;

public class Rabbit {
    private static int rabbitCount = 0;

    private int id;
    private double ageInYears; // Age in years
    private boolean isPregnant;
    private Sex sex;

    private double survivalRate;

    public Rabbit(double ageInYears, Sex sex) {
        this.id = ++rabbitCount;
        this.ageInYears = ageInYears;
        this.isPregnant = false;
        this.sex = sex;
        this.survivalRate = calculateSurvivalRate(ageInYears);
    }

    public void incrementAge() {
        this.ageInYears += 1.0; // Increase age by 1 year
        survivalRate = calculateSurvivalRate(ageInYears);
    }

    public boolean canGiveBirth() {
        return sex == Sex.FEMALE && ageInYears >= 1;
    }

    public boolean isAlive(MTRandom random) {
        double randomNumber = random.nextDouble();
        return randomNumber > survivalRate;
    }

    public boolean isPregnant() {
        return isPregnant;
    }

    public void gettingPregnant() {
        if (canGiveBirth() && !isPregnant) {
            isPregnant = true;
        }
    }

    public List<Rabbit> giveBirth(MTRandom random) {
        int numberOfLitters = 1;
        int numberOfKittens = 0;

        double rg = random.nextDouble();

        if (rg < 0.1) {
            numberOfLitters = 4;
        } else if (rg < 0.5) {
            numberOfLitters = 5;
        } else if (rg < 0.8) {
            numberOfLitters = 6;
        } else {
            numberOfLitters = 7;
        }

        List<Rabbit> newKittens = new ArrayList<>();

        for (int i = 0; i < numberOfLitters; i++) {
            numberOfKittens = 3 + random.nextInt(4); // Equal chance to have 3 to 6 kittens per litter

            for (int j = 0; j < numberOfKittens; j++) {
                Sex kittenSex = determineSexOfKitten(random);
                Rabbit kitten = new Rabbit(0, kittenSex);
                newKittens.add(kitten);
            }
        }

        this.isPregnant = false;

        return newKittens;
    }

    public static Sex determineSexOfKitten(MTRandom random) {
        double naturalProbability = 0.5;
        double rg = random.nextDouble();

        if (rg < naturalProbability) {
            return Sex.MALE;
        } else {
            return Sex.FEMALE;
        }
    }

    private double calculateSurvivalRate(double ageInYears) {
        double survivalRate = 0.0;

        if (ageInYears < 1) {
            // Survival rate for young rabbits (35%)
            survivalRate = 0.35;
        } else if (ageInYears >= 5.0 && ageInYears < 10.0) {
            // Survival rate for adult rabbits (60%)
            survivalRate = 0.6;
        } else if (ageInYears >= 10.0 && ageInYears < 15.0) {
            // Diminish survival rate by 10% every year after age 10
            double diminishingRate = (ageInYears - 10.0) * 0.1;
            survivalRate = 0.6 - diminishingRate;
        } else {
            // Survival rate is 0% for rabbits older than 15 years
            survivalRate = 0.0;
        }

        return survivalRate;
    }

    @Override
    public String toString() {
        String displayMsg = "[Rabbit]\n" + "\nNum. : " + this.id + "\nAge (years) : " + this.ageInYears
                + "\nSex : " + this.sex;

        if (this.sex.equals(Sex.FEMALE)) {
            displayMsg += "\nPregnant : " + this.isPregnant;
        }

        return displayMsg;
    }

    public static int getRabbitCount() {
        return rabbitCount;
    }

    public static void setRabbitCount(int rabbitCount) {
        Rabbit.rabbitCount = rabbitCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAgeInYears() {
        return ageInYears;
    }

    public void setAgeInYears(double ageInYears) {
        this.ageInYears = ageInYears;
    }

    public void setPregnant(boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
