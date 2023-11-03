package src;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RabbitPopulationGraph extends JPanel {
    private List<Integer> populationData;
    private List<Integer> predatorData;
    private List<Integer> babyRabbitData;

    public RabbitPopulationGraph(List<Integer> populationData, List<Integer> predatorData, List<Integer> babyRabbitData) {
        this.populationData = populationData;
        this.predatorData = predatorData;
        this.babyRabbitData = babyRabbitData;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int barWidth = 20;
        int barSpacing = 10;
        int x = 10;
        
        int maxPopulation = populationData.stream().max(Integer::compareTo).orElse(1);
        
        for (int i = 0; i < populationData.size(); i++) {
            int barHeight = (int) (populationData.get(i) / (double) maxPopulation * (getHeight() - 30));
            g.setColor(Color.GREEN); 
            g.fillRect(x, getHeight() - 20 - barHeight, barWidth, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, getHeight() - 20 - barHeight, barWidth, barHeight);
        
            int babyHeight = (int) (babyRabbitData.get(i) / (double) maxPopulation * (getHeight() - 30));
            g.setColor(Color.PINK);
            g.fillRect(x, getHeight() - 20 - barHeight - babyHeight, barWidth, babyHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, getHeight() - 20 - barHeight - babyHeight, barWidth, babyHeight);

        
            int predatorHeight = (int) (predatorData.get(i) / (double) maxPopulation * (getHeight() - 30));
            g.setColor(Color.RED);
            g.fillRect(x, getHeight() - 20 - barHeight - babyHeight - predatorHeight, barWidth, predatorHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, getHeight() - 20 - barHeight - babyHeight - predatorHeight, barWidth, predatorHeight);


            g.setColor(Color.BLACK);
            g.drawString("M" + (i + 1), x, getHeight() - 15);

            x += barWidth + barSpacing;
        }
        
        // Légende
        g.setColor(Color.BLACK);
        g.drawString("* Adultes Lapins (Vert)", super.getWidth() - 300,  25);
        g.setColor(Color.BLACK);
        g.drawString("* Jeunes Lapins (Rose)", super.getWidth() - 300 , 35);
        g.setColor(Color.BLACK);
        g.drawString("* Predateurs (Rouge)", super.getWidth() - 300,  45);
    }

}
