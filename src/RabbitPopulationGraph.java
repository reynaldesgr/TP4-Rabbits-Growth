package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class RabbitPopulationGraph extends JPanel {
    private List<Integer> populationData;
    private List<Integer> predatorData;
    private List<Integer> babyRabbitData;
    private List<Integer> maleData;
    private List<Integer> femaleData;

    public RabbitPopulationGraph(List<Integer> populationData, List<Integer> predatorData, List<Integer> babyRabbitData, List<Integer> maleData, List<Integer> femaleData) {
        this.populationData = populationData;
        this.predatorData = predatorData;
        this.babyRabbitData = babyRabbitData;
        this.maleData = maleData;
        this.femaleData = femaleData;

        // Ajoutez un ComponentListener pour gérer les redimensionnements de la fenêtre
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint(); // Redessiner le graphique lorsque la fenêtre est redimensionnée
            }
        });
    }

    @Override
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
            g.setColor(Color.YELLOW);
            g.fillRect(x, getHeight() - 20 - barHeight - babyHeight, barWidth, babyHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, getHeight() - 20 - barHeight - babyHeight, barWidth, babyHeight);

            int predatorHeight = (int) (predatorData.get(i) / (double) maxPopulation * (getHeight() - 30));
            g.setColor(Color.RED);
            g.fillRect(x, getHeight() - 20 - barHeight - babyHeight - predatorHeight, barWidth, predatorHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, getHeight() - 20 - barHeight - babyHeight - predatorHeight, barWidth, predatorHeight);

            int maleCount = maleData.get(i);
            int femaleCount = femaleData.get(i);
            int totalRabbits = maleCount + femaleCount;

            double malePercentage = (maleCount / (double) totalRabbits);
            double femalePercentage = (femaleCount / (double) totalRabbits);

            int circleRadius = (int) (barWidth * 0.5); 
            int centerX = x + barWidth / 2;
            int centerY = getHeight() - 20 - barHeight - babyHeight - predatorHeight - circleRadius;

            // Dessiner le camembert
            double startAngle = 0;
            double maleArcAngle = malePercentage * 360;
            double femaleArcAngle = femalePercentage * 360;

            g.setColor(Color.BLUE);
            g.fillArc(centerX - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius, (int) startAngle, (int) maleArcAngle);

            g.setColor(Color.PINK);
            g.fillArc(centerX - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius, (int) (startAngle + maleArcAngle), (int) femaleArcAngle);

            g.setColor(Color.BLACK);
            g.drawArc(centerX - circleRadius, centerY - circleRadius, 2 * circleRadius, 2 * circleRadius, (int) startAngle, 360);

            g.setColor(Color.BLACK);
            g.drawString("M" + (i + 1), x, getHeight() - 15);

            x += barWidth + barSpacing;
        }

        // Légende
        g.setColor(Color.BLACK);
        g.drawString("Adultes Lapins (Vert)", getWidth() - 300, 25);
        g.setColor(Color.BLACK);
        g.drawString("Jeunes Lapins (Jaune)", getWidth() - 300, 35);
        g.setColor(Color.BLACK);
        g.drawString("Predateurs (Rouge)", getWidth() - 300, 45);
        g.setColor(Color.BLACK);
        g.drawString("Proportion de Males (Bleu) et de Femelles (Rose)", getWidth() - 300, 55);
    }

}
